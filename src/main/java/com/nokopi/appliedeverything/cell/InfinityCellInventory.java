package com.nokopi.appliedeverything.cell;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Supplies an unlimited amount of the imprinted key (an item or a fluid) and voids any of it that is inserted.
 */
public class InfinityCellInventory implements StorageCell {
    /** The amount reported to the network. AE2's creative cell uses the same value. */
    public static final long REPORTED_AMOUNT = Integer.MAX_VALUE;

    private final ItemStack stack;
    private final AEKey key;

    public InfinityCellInventory(ItemStack stack, AEKey key) {
        this.stack = stack;
        this.key = key;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        return key.equals(what) ? amount : 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        return key.equals(what) ? amount : 0;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        out.add(key, REPORTED_AMOUNT);
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return key.equals(what);
    }

    @Override
    public CellState getStatus() {
        return CellState.TYPES_FULL;
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public boolean canFitInsideCell() {
        return false;
    }

    @Override
    public Component getDescription() {
        return stack.getHoverName();
    }

    @Override
    public void persist() {
        // Nothing to save: the contents never change
    }
}
