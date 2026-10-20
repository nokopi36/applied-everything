package com.nokopi.appliedeverything.cell;

import org.jetbrains.annotations.Nullable;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.world.item.ItemStack;

public class InfinityCellHandler implements ICellHandler {
    public static final InfinityCellHandler INSTANCE = new InfinityCellHandler();

    @Override
    public boolean isCell(ItemStack is) {
        return is.getItem() instanceof ImprintedCell cell && cell.getImprintedKey(is) != null;
    }

    @Override
    @Nullable
    public StorageCell getCellInventory(ItemStack is, @Nullable ISaveProvider host) {
        if (!(is.getItem() instanceof ImprintedCell cell)) {
            return null;
        }
        var key = cell.getImprintedKey(is);
        return key != null ? new InfinityCellInventory(is, key) : null;
    }
}
