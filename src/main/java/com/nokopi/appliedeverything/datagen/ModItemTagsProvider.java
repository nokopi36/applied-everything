package com.nokopi.appliedeverything.datagen;

import java.util.concurrent.CompletableFuture;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.ModTags;

import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagEntry;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AppliedEverything.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        var blacklist = tag(ModTags.IMPRINT_BLACKLIST);
        // All of this mod's items, so the cost chain cannot be skipped
        ModItems.ITEMS.getEntries().forEach(item -> {
            if (item == ModItems.INFINITY_ENERGY_CELL) {
                // Only exists with Applied Flux: listed as optional so the tag still loads without it
                blacklist.add(TagEntry.optionalElement(item.getId()));
            } else {
                blacklist.add(item.get());
            }
        });
        // An infinite singularity supply would make every further cell cost only power
        blacklist.add(AEItems.SINGULARITY.asItem());
    }
}
