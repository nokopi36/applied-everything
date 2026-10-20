package com.nokopi.appliedeverything;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    /**
     * Items that cannot be imprinted into an Infinity Cell. Defaults to this mod's items and AE2's singularity;
     * modpacks can add or remove entries with a datapack.
     */
    public static final TagKey<Item> IMPRINT_BLACKLIST = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(AppliedEverything.MODID, "imprint_blacklist"));

    private ModTags() {
    }
}
