package com.nokopi.appliedeverything.datagen;

import java.util.concurrent.CompletableFuture;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModItems;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        var items = this.registries.lookupOrThrow(Registries.ITEM);

        // Compressed Singularities are made only in the Singularity Compressor

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.INFINITY_CORE.get())
                .pattern("CCC")
                .pattern("CNC")
                .pattern("CCC")
                .define('C', ModItems.COMPRESSED_SINGULARITY.get())
                .define('N', Items.NETHER_STAR)
                .unlockedBy("has_compressed_singularity", this.has(ModItems.COMPRESSED_SINGULARITY.get()))
                .save(this.output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.BLANK_INFINITY_CELL.get())
                .pattern("CPC")
                .pattern("PIP")
                .pattern("CPH")
                .define('I', ModItems.INFINITY_CORE.get())
                .define('P', AEItems.CELL_COMPONENT_256K)
                .define('C', ModItems.COMPRESSED_SINGULARITY.get())
                .define('H', AEItems.ITEM_CELL_HOUSING)
                .unlockedBy("has_infinity_core", this.has(ModItems.INFINITY_CORE.get()))
                .save(this.output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.SINGULARITY_COMPRESSOR.get())
                .pattern("SPS")
                .pattern("DID")
                .pattern("SPS")
                .define('I', AEBlocks.INSCRIBER)
                .define('D', AEBlocks.DENSE_ENERGY_CELL)
                .define('P', AEItems.ENGINEERING_PROCESSOR)
                .define('S', AEItems.SINGULARITY)
                .unlockedBy("has_singularity", this.has(AEItems.SINGULARITY))
                .save(this.output);

        // Same as the blank Infinity Cell, with a fluid cell housing instead of the item cell housing
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.BLANK_INFINITY_FLUID_CELL.get())
                .pattern("CPC")
                .pattern("PIP")
                .pattern("CPF")
                .define('I', ModItems.INFINITY_CORE.get())
                .define('P', AEItems.CELL_COMPONENT_256K)
                .define('C', ModItems.COMPRESSED_SINGULARITY.get())
                .define('F', AEItems.FLUID_CELL_HOUSING)
                .unlockedBy("has_infinity_core", this.has(ModItems.INFINITY_CORE.get()))
                .save(this.output);

        // Imprinting happens in the Imprinter, not in a crafting grid
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.IMPRINTER.get())
                .pattern("SLS")
                .pattern("EWE")
                .pattern("SLS")
                .define('W', AEBlocks.CELL_WORKBENCH)
                .define('L', AEItems.LOGIC_PROCESSOR)
                .define('E', AEItems.ENGINEERING_PROCESSOR)
                .define('S', AEItems.SINGULARITY)
                .unlockedBy("has_singularity", this.has(AEItems.SINGULARITY))
                .save(this.output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "Applied Everything Recipes";
        }
    }
}
