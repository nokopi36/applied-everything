package com.nokopi.appliedeverything.machine;

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
import appeng.util.inv.filter.IAEItemFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SingularityCompressorBlockEntity extends AENetworkedPoweredBlockEntity
        implements ServerTickingBlockEntity, IUpgradeableObject {
    /** AE consumed by one compression. */
    public static final double ENERGY_PER_OPERATION = 10_000_000;
    /** Singularities consumed by one compression. */
    public static final int SINGULARITIES_PER_OPERATION = 9;
    /** AE drawn per tick without acceleration cards. */
    public static final double BASE_DRAW_PER_TICK = 5_000;
    /** Additional AE drawn per tick for each installed acceleration card. */
    public static final double DRAW_PER_CARD = 5_000;
    public static final int MAX_ACCELERATION_CARDS = 4;
    /** The internal buffer holds this many ticks of the current draw, so large amounts cannot be stockpiled. */
    public static final int BUFFER_TICKS = 2;

    private final AppEngInternalInventory input = new AppEngInternalInventory(this, 1, 64, new InputFilter());
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1, 64);
    private final InternalInventory inventory = new CombinedInternalInventory(input, output);
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
    public IUpgradeInventory getUpgrades() {
        return upgrades;
    }

    public double getConsumedEnergy() {
        return consumedEnergy;
    }

    /** AE drawn per tick with the currently installed acceleration cards. */
    public double getDrawPerTick() {
        return BASE_DRAW_PER_TICK + DRAW_PER_CARD * upgrades.getInstalledUpgrades(AEItems.SPEED_CARD);
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
        if (inv == input && input.getStackInSlot(0).getCount() < SINGULARITIES_PER_OPERATION && consumedEnergy > 0) {
            consumedEnergy = 0;
            saveChanges();
        }
    }

    private boolean canWork() {
        if (input.getStackInSlot(0).getCount() < SINGULARITIES_PER_OPERATION) {
            return false;
        }
        var result = new ItemStack(ModItems.COMPRESSED_SINGULARITY.get());
        return output.insertItem(0, result, true).isEmpty();
    }

    @Override
    public void serverTick() {
        if (!canWork()) {
            return;
        }

        // Use the internal buffer (filled by FE) first, then pull the rest from the ME network
        var wanted = Math.min(getDrawPerTick(), ENERGY_PER_OPERATION - consumedEnergy);
        var got = extractAEPower(wanted, Actionable.MODULATE, PowerMultiplier.ONE);
        if (got < wanted) {
            var grid = getMainNode().getGrid();
            if (grid != null) {
                got += grid.getEnergyService().extractAEPower(wanted - got, Actionable.MODULATE, PowerMultiplier.ONE);
            }
        }
        if (got <= 0) {
            return;
        }

        consumedEnergy += got;
        if (consumedEnergy >= ENERGY_PER_OPERATION - 0.001) {
            consumedEnergy = 0;
            input.extractItem(0, SINGULARITIES_PER_OPERATION, false);
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

    private static class InputFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return AEItems.SINGULARITY.is(stack);
        }
    }
}
