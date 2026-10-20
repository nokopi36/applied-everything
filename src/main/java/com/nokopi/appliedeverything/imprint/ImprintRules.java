package com.nokopi.appliedeverything.imprint;

import com.nokopi.appliedeverything.ModTags;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

/**
 * Decides which items can be imprinted into an Infinity Cell.
 */
public final class ImprintRules {
    private ImprintRules() {
    }

    public static boolean canImprint(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Configurable through the item tag (defaults: this mod's items and AE2's singularity)
        if (stack.is(ModTags.IMPRINT_BLACKLIST)) {
            return false;
        }
        // Only items in their default state: blocks duplicating contents (shulker boxes, cells),
        // enchantments, custom names, damage and the like. Not configurable, since it is about state, not item type.
        return stack.isComponentsPatchEmpty();
    }

    public static boolean canImprint(FluidResource fluid) {
        if (fluid.isEmpty()) {
            return false;
        }
        if (fluid.getFluid().builtInRegistryHolder().is(ModTags.FLUID_IMPRINT_BLACKLIST)) {
            return false;
        }
        // Fluids carrying extra data are rejected, like modified items
        return fluid.getComponentsPatch().isEmpty();
    }
}
