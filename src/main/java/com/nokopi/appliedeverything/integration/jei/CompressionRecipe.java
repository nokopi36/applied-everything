package com.nokopi.appliedeverything.integration.jei;

import net.minecraft.world.item.ItemStack;

/** One Singularity Compressor operation, for display in JEI. */
public record CompressionRecipe(ItemStack input, ItemStack output, double energy) {
}
