package com.nokopi.appliedeverything.datagen;

import java.util.concurrent.CompletableFuture;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;

public class ModFluidTagsProvider extends FluidTagsProvider {
    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AppliedEverything.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Empty by default; the file exists so modpacks have something to extend
        tag(ModTags.FLUID_IMPRINT_BLACKLIST);
    }
}
