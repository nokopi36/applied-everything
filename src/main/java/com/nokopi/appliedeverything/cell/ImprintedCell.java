package com.nokopi.appliedeverything.cell;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.AEKey;
import net.minecraft.world.item.ItemStack;

/** An item that acts as an Infinity Cell for whatever key it has been imprinted with. */
public interface ImprintedCell {
    /** The key this cell supplies, or null if it has not been imprinted. */
    @Nullable
    AEKey getImprintedKey(ItemStack stack);
}
