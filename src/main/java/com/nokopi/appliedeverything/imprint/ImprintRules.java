package com.nokopi.appliedeverything.imprint;

import com.nokopi.appliedeverything.ModTags;

import net.minecraft.world.item.ItemStack;

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
}
