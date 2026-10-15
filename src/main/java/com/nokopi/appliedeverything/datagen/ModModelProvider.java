package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModItems;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, AppliedEverything.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.COMPRESSED_SINGULARITY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.INFINITY_CORE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLANK_INFINITY_CELL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.INFINITY_CELL.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialBlock(ModBlocks.SINGULARITY_COMPRESSOR.get(), TexturedModel.CUBE_TOP_BOTTOM);
    }
}
