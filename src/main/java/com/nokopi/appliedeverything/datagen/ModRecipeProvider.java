package com.nokopi.appliedeverything.datagen;

import java.util.concurrent.CompletableFuture;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.imprint.ImprintRecipe;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        var items = this.registries.lookupOrThrow(Registries.ITEM);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.COMPRESSED_SINGULARITY.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', AEItems.SINGULARITY)
                .unlockedBy("has_singularity", this.has(AEItems.SINGULARITY))
                .save(this.output);

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

        SpecialRecipeBuilder.special(() -> ImprintRecipe.INSTANCE)
                .save(this.output, AppliedEverything.MODID + ":imprint");
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
