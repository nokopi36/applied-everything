package com.nokopi.appliedeverything.machine;

import com.nokopi.appliedeverything.ModServerConfig;
import java.util.List;

import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModItems;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.inventories.InternalInventory;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.blockentity.grid.AENetworkedPoweredBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.CombinedInternalInventory;
import appeng.util.inv.FilteredInternalInventory;
import appeng.util.inv.filter.IAEItemFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SingularityCompressorBlockEntity extends AENetworkedPoweredBlockEntity
        implements ServerTickingBlockEntity, IUpgradeableObject {
    // Singularities and AE per compression and the draw per tick are in ModServerConfig
    public static final int MAX_ACCELERATION_CARDS = 4;
    /** The internal buffer holds this many ticks of the current draw, so large amounts cannot be stockpiled. */
    public static final int BUFFER_TICKS = 2;

    private final AppEngInternalInventory input = new AppEngInternalInventory(this, 1, 64, new InputFilter());
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1, 64);
    private final InternalInventory inventory = new CombinedInternalInventory(input, output);
    /** What hoppers, pipes and AE2 buses see: insert into the input, extract from the output. */
    private final InternalInventory automationInventory = new FilteredInternalInventory(inventory, new AutomationFilter());
    private final IUpgradeInventory upgrades;

    /** AE consumed towards the current compression. */
    private double consumedEnergy;

    public SingularityCompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.getMainNode().setIdlePowerUsage(0);
        this.upgrades = UpgradeInventories.forMachine(ModBlocks.SINGULARITY_COMPRESSOR.get(), MAX_ACCELERATION_CARDS,
                this::onUpgradesChanged);
        updateBufferSize();
    }

    public InternalInventory getInput() {
        return input;
    }

    public InternalInventory getOutput() {
        return output;
    }

    @Override
    public InternalInventory getInternalInventory() {
        return inventory;
    }

    @Override
    protected InternalInventory getExposedInventoryForSide(Direction side) {
        return automationInventory;
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return upgrades;
    }

    /** Progress of the current compression in percent. */
    public int getProgressPercent() {
        return (int) (consumedEnergy * 100 / ModServerConfig.energyPerCompression());
    }

    public double getConsumedEnergy() {
        return consumedEnergy;
    }

    /** AE drawn per tick with the currently installed acceleration cards. */
    public double getDrawPerTick() {
        return ModServerConfig.compressorBaseDraw() + ModServerConfig.compressorDrawPerCard() * upgrades.getInstalledUpgrades(AEItems.SPEED_CARD);
    }

    private void onUpgradesChanged() {
        updateBufferSize();
        saveChanges();
    }

    private void updateBufferSize() {
        setInternalMaxPower(getDrawPerTick() * BUFFER_TICKS);
    }

    @Override
    public void onChangeInventory(AppEngInternalInventory inv, int slot) {
        // Taking singularities out abandons the current compression; the energy spent so far is lost
        if (inv == input && input.getStackInSlot(0).getCount() < ModServerConfig.singularitiesPerCompression() && consumedEnergy > 0) {
            consumedEnergy = 0;
            saveChanges();
        }
    }

    private boolean canWork() {
        if (input.getStackInSlot(0).getCount() < ModServerConfig.singularitiesPerCompression()) {
            return false;
        }
        var result = new ItemStack(ModItems.COMPRESSED_SINGULARITY.get());
        return output.insertItem(0, result, true).isEmpty();
    }

    @Override
    public void serverTick() {
        // Follows config changes made while the world is running
        if (getInternalMaxPower() != getDrawPerTick() * BUFFER_TICKS) {
            updateBufferSize();
        }
        if (!canWork()) {
            FrameMachineBlock.setWorking(this, false);
            return;
        }

        // Use the internal buffer (filled by FE) first, then pull the rest from the ME network
        var wanted = Math.min(getDrawPerTick(), ModServerConfig.energyPerCompression() - consumedEnergy);
        var got = extractAEPower(wanted, Actionable.MODULATE, PowerMultiplier.ONE);
        if (got < wanted) {
            var grid = getMainNode().getGrid();
            if (grid != null) {
                got += grid.getEnergyService().extractAEPower(wanted - got, Actionable.MODULATE, PowerMultiplier.ONE);
            }
        }
        if (got <= 0) {
            FrameMachineBlock.setWorking(this, false);
            return;
        }

        FrameMachineBlock.setWorking(this, true);
        consumedEnergy += got;
        if (consumedEnergy >= ModServerConfig.energyPerCompression() - 0.001) {
            consumedEnergy = 0;
            input.extractItem(0, ModServerConfig.singularitiesPerCompression(), false);
            output.insertItem(0, new ItemStack(ModItems.COMPRESSED_SINGULARITY.get()), false);
        }
        saveChanges();
    }

    @Override
    public void saveAdditional(ValueOutput data) {
        super.saveAdditional(data);
        upgrades.writeToNBT(data, "upgrades");
        data.putDouble("consumed_energy", consumedEnergy);
    }

    @Override
    public void loadTag(ValueInput data) {
        super.loadTag(data);
        upgrades.readFromNBT(data, "upgrades");
        consumedEnergy = data.getDoubleOr("consumed_energy", 0);
        updateBufferSize();
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        super.addAdditionalDrops(level, pos, drops);
        for (var upgrade : upgrades) {
            drops.add(upgrade);
        }
    }

    @Override
    public void clearContent() {
        super.clearContent();
        upgrades.clear();
    }

    /** Slot 0 is the input and slot 1 the output of the combined inventory. */
    private static class AutomationFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return slot == 0 && AEItems.SINGULARITY.is(stack);
        }

        @Override
        public boolean allowExtract(InternalInventory inv, int slot, int amount) {
            return slot == 1;
        }
    }

    private static class InputFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return AEItems.SINGULARITY.is(stack);
        }
    }
}
