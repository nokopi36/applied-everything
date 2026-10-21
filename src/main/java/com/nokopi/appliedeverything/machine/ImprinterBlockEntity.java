package com.nokopi.appliedeverything.machine;

import org.jetbrains.annotations.Nullable;

import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellItem;
import com.nokopi.appliedeverything.cell.InfinityFluidCellItem;
import com.nokopi.appliedeverything.imprint.FluidContainers;
import com.nokopi.appliedeverything.imprint.ImprintRules;
import com.nokopi.appliedeverything.integration.appflux.InfinityEnergyCellItem;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.inventories.InternalInventory;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.blockentity.grid.AENetworkedPoweredBlockEntity;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.CombinedInternalInventory;
import appeng.util.inv.FilteredInternalInventory;
import appeng.util.inv.filter.IAEItemFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ImprinterBlockEntity extends AENetworkedPoweredBlockEntity implements ServerTickingBlockEntity {
    /** AE consumed by one imprint. */
    public static final double ENERGY_PER_IMPRINT = 100_000;
    /** Ticks one imprint takes when power keeps up. */
    public static final int TICKS_PER_IMPRINT = 100;
    public static final double DRAW_PER_TICK = ENERGY_PER_IMPRINT / TICKS_PER_IMPRINT;
    /** AE per tick pushed into an Infinity Energy Cell being charged. */
    public static final double CHARGE_RATE_AE = 50_000;

    private final AppEngInternalInventory cellSlot = new AppEngInternalInventory(this, 1, 1, new CellFilter());
    private final AppEngInternalInventory itemSlot = new AppEngInternalInventory(this, 1, 64, new ItemFilter());
    private final AppEngInternalInventory containerSlot = new AppEngInternalInventory(this, 1, 1, new ContainerFilter());
    private final AppEngInternalInventory outputSlot = new AppEngInternalInventory(this, 1, 1);
    private final InternalInventory inventory = new CombinedInternalInventory(cellSlot, itemSlot, containerSlot, outputSlot);
    /** What hoppers, pipes and AE2 buses see. */
    private final InternalInventory automationInventory = new FilteredInternalInventory(inventory, new AutomationFilter());
    private final ImprintTank tank = new ImprintTank(this::saveChanges, this::acceptsFluid);

    /** AE consumed towards the current imprint. */
    private double consumedEnergy;

    public ImprinterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.getMainNode().setIdlePowerUsage(0);
        this.setInternalMaxPower(DRAW_PER_TICK * 2);
    }

    public InternalInventory getCellSlot() {
        return cellSlot;
    }

    public InternalInventory getItemSlot() {
        return itemSlot;
    }

    public InternalInventory getContainerSlot() {
        return containerSlot;
    }

    public InternalInventory getOutputSlot() {
        return outputSlot;
    }

    public ImprintTank getTank() {
        return tank;
    }

    public double getConsumedEnergy() {
        return consumedEnergy;
    }

    /** Progress of the current job in percent: the imprint, or the charge of an Infinity Energy Cell. */
    public int getProgressPercent() {
        var cell = cellSlot.getStackInSlot(0);
        if (isEnergyCell(cell)) {
            return (int) ((long) InfinityEnergyCellItem.getStoredFe(cell) * 100 / InfinityEnergyCellItem.CAPACITY_FE);
        }
        return (int) (consumedEnergy * 100 / ENERGY_PER_IMPRINT);
    }

    @Override
    public InternalInventory getInternalInventory() {
        return inventory;
    }

    @Override
    protected InternalInventory getExposedInventoryForSide(Direction side) {
        return automationInventory;
    }

    /** Items are refused while a blank fluid cell waits in the cell slot, so they cannot be put in by mistake. */
    private boolean acceptsItem() {
        return !cellSlot.getStackInSlot(0).is(ModItems.BLANK_INFINITY_FLUID_CELL.get());
    }

    /** Fluids are refused while a blank item cell waits in the cell slot. */
    private boolean acceptsFluid() {
        return !cellSlot.getStackInSlot(0).is(ModItems.BLANK_INFINITY_CELL.get());
    }

    /** The cell the current inputs would produce, or null if nothing can be imprinted right now. */
    @Nullable
    private ItemStack pendingResult() {
        if (!outputSlot.getStackInSlot(0).isEmpty()) {
            return null;
        }
        var cell = cellSlot.getStackInSlot(0);
        if (cell.is(ModItems.BLANK_INFINITY_CELL.get())) {
            var item = itemSlot.getStackInSlot(0);
            return !item.isEmpty() && ImprintRules.canImprint(item) ? InfinityCellItem.imprinted(AEItemKey.of(item)) : null;
        }
        if (cell.is(ModItems.BLANK_INFINITY_FLUID_CELL.get())) {
            AEFluidKey fluid = tank.getFluid();
            return fluid != null && tank.isFull() ? InfinityFluidCellItem.imprinted(fluid) : null;
        }
        return null;
    }

    @Override
    public void serverTick() {
        emptyContainerIntoTank();

        if (isEnergyCell(cellSlot.getStackInSlot(0))) {
            chargeEnergyCell();
            return;
        }

        var result = pendingResult();
        if (result == null) {
            consumedEnergy = 0;
            FrameMachineBlock.setWorking(this, false);
            return;
        }

        var wanted = Math.min(DRAW_PER_TICK, ENERGY_PER_IMPRINT - consumedEnergy);
        var got = extractAEPower(wanted, Actionable.MODULATE, PowerMultiplier.ONE);
        if (got < wanted) {
            var grid = getMainNode().getGrid();
            if (grid != null) {
                got += grid.getEnergyService().extractAEPower(wanted - got, Actionable.MODULATE, PowerMultiplier.ONE);
            }
        }
        FrameMachineBlock.setWorking(this, got > 0);
        if (got <= 0) {
            return;
        }

        consumedEnergy += got;
        if (consumedEnergy >= ENERGY_PER_IMPRINT - 0.001) {
            consumedEnergy = 0;
            if (result.is(ModItems.INFINITY_FLUID_CELL.get())) {
                tank.setStack(0, null);
            } else {
                itemSlot.extractItem(0, 1, false);
            }
            cellSlot.setItemDirect(0, ItemStack.EMPTY);
            outputSlot.setItemDirect(0, result);
        }
        saveChanges();
    }

    /** Charges an Infinity Energy Cell from the network, and moves it to the output once full. */
    private void chargeEnergyCell() {
        var cell = cellSlot.getStackInSlot(0);
        if (InfinityEnergyCellItem.isFullyCharged(cell)) {
            FrameMachineBlock.setWorking(this, false);
            if (outputSlot.getStackInSlot(0).isEmpty()) {
                outputSlot.setItemDirect(0, cell);
                cellSlot.setItemDirect(0, ItemStack.EMPTY);
                saveChanges();
            }
            return;
        }
        var missingAe = (InfinityEnergyCellItem.CAPACITY_FE - InfinityEnergyCellItem.getStoredFe(cell)) / InfinityEnergyCellItem.FE_PER_AE;
        var wanted = Math.min(CHARGE_RATE_AE, missingAe);
        var got = extractAEPower(wanted, Actionable.MODULATE, PowerMultiplier.ONE);
        if (got < wanted) {
            var grid = getMainNode().getGrid();
            if (grid != null) {
                got += grid.getEnergyService().extractAEPower(wanted - got, Actionable.MODULATE, PowerMultiplier.ONE);
            }
        }
        FrameMachineBlock.setWorking(this, got > 0);
        if (got > 0) {
            var charged = cell.copy();
            InfinityEnergyCellItem.charge(charged, (long) Math.ceil(got * InfinityEnergyCellItem.FE_PER_AE), false);
            cellSlot.setItemDirect(0, charged);
            saveChanges();
        }
    }

    /** Pours a filled container into the tank, leaving the empty container in its slot. */
    private void emptyContainerIntoTank() {
        var container = containerSlot.getStackInSlot(0);
        if (container.isEmpty() || tank.isFull()) {
            return;
        }
        int room = ImprintTank.CAPACITY - (int) tank.getAmount(0);
        var drained = FluidContainers.drain(container, room);
        if (drained == null) {
            return;
        }
        var fluid = AEFluidKey.of(drained.fluid());
        if (fluid == null || tank.insert(0, fluid, drained.amount(), Actionable.SIMULATE) < drained.amount()) {
            return;
        }
        tank.insert(0, fluid, drained.amount(), Actionable.MODULATE);
        containerSlot.setItemDirect(0, drained.emptied());
    }

    @Override
    public void saveAdditional(ValueOutput data) {
        super.saveAdditional(data);
        tank.writeToChildTag(data, "tank");
        data.putDouble("consumed_energy", consumedEnergy);
    }

    @Override
    public void loadTag(ValueInput data) {
        super.loadTag(data);
        tank.readFromChildTag(data, "tank");
        consumedEnergy = data.getDoubleOr("consumed_energy", 0);
    }

    private static boolean isEnergyCell(ItemStack stack) {
        return ModItems.INFINITY_ENERGY_CELL != null && stack.is(ModItems.INFINITY_ENERGY_CELL.get());
    }

    /** Cells the imprinter works on: blank cells, and Infinity Energy Cells that still need charging. */
    private static boolean isBlankCell(ItemStack stack) {
        return stack.is(ModItems.BLANK_INFINITY_CELL.get()) || stack.is(ModItems.BLANK_INFINITY_FLUID_CELL.get())
                || isEnergyCell(stack) && !InfinityEnergyCellItem.isFullyCharged(stack);
    }

    private static class CellFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return isBlankCell(stack);
        }
    }

    private class ItemFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return acceptsItem() && ImprintRules.canImprint(stack);
        }
    }

    private class ContainerFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return acceptsFluid();
        }
    }

    /** Slots of the combined inventory: 0 cell, 1 item, 2 container, 3 output. */
    private static class AutomationFilter implements IAEItemFilter {
        @Override
        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return slot == 0 ? isBlankCell(stack) : slot == 1 && !isBlankCell(stack);
        }

        @Override
        public boolean allowExtract(InternalInventory inv, int slot, int amount) {
            if (slot == 3) {
                return true;
            }
            // Containers can be taken out once they are empty
            return slot == 2 && FluidContainers.drain(inv.getStackInSlot(slot), 1) == null;
        }
    }
}
