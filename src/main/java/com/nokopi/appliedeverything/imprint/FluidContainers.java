package com.nokopi.appliedeverything.imprint;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/** Reads and empties fluid containers (buckets, tanks from other mods) held as items. */
public final class FluidContainers {
    private FluidContainers() {
    }

    /** What a container held and what it becomes once emptied. */
    public record Drained(FluidResource fluid, int amount, ItemStack emptied) {
    }

    /**
     * Empties a copy of one container of its first fluid, taking at most {@code maxAmount}. The given stack is not
     * changed. Returns null if it holds no fluid that could be drained.
     */
    @Nullable
    public static Drained drain(ItemStack container, int maxAmount) {
        // A one-slot inventory, so containers that swap themselves (a water bucket becoming a bucket) can do so
        var holder = new ItemStacksResourceHandler(NonNullList.of(ItemStack.EMPTY, container.copyWithCount(1)));
        var handler = ItemAccess.forHandlerIndex(holder, 0).getCapability(Capabilities.Fluid.ITEM);
        if (handler == null) {
            return null;
        }
        for (int i = 0; i < handler.size(); i++) {
            var fluid = handler.getResource(i);
            int amount = Math.min(handler.getAmountAsInt(i), maxAmount);
            if (fluid.isEmpty() || amount <= 0) {
                continue;
            }
            int drained;
            try (var tx = Transaction.openRoot()) {
                drained = handler.extract(fluid, amount, tx);
                if (drained <= 0) {
                    continue;
                }
                tx.commit();
            }
            int left = holder.getAmountAsInt(0);
            var emptied = left > 0 ? holder.getResource(0).toStack(left) : ItemStack.EMPTY;
            return new Drained(fluid, drained, emptied);
        }
        return null;
    }
}
