package com.nokopi.appliedeverything.imprint;

import com.nokopi.appliedeverything.AppliedEverything;

import net.minecraft.core.registries.BuiltInRegistries;
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
        // This mod's own items are excluded to keep the cost chain meaningful
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(AppliedEverything.MODID)) {
            return false;
        }
        // Only items in their default state: blocks duplicating contents (shulker boxes, cells),
        // enchantments, custom names, damage and the like
        return stack.isComponentsPatchEmpty();
    }
}
