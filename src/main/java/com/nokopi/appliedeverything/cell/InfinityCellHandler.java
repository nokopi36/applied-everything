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
        return is.getItem() instanceof InfinityCellItem && InfinityCellItem.getImprintedItem(is) != null;
    }

    @Override
    @Nullable
    public StorageCell getCellInventory(ItemStack is, @Nullable ISaveProvider host) {
        if (!(is.getItem() instanceof InfinityCellItem)) {
            return null;
        }
        var item = InfinityCellItem.getImprintedItem(is);
        return item != null ? new InfinityCellInventory(is, item) : null;
    }
}
