package com.nokopi.appliedeverything.datagen;

import java.util.concurrent.CompletableFuture;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.ModTags;

import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AppliedEverything.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        var blacklist = tag(ModTags.IMPRINT_BLACKLIST);
        // All of this mod's items, so the cost chain cannot be skipped
        ModItems.ITEMS.getEntries().forEach(item -> blacklist.add(item.get()));
        // An infinite singularity supply would make every further cell cost only power
        blacklist.add(AEItems.SINGULARITY.asItem());
    }
}
