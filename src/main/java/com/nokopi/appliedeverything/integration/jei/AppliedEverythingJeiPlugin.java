package com.nokopi.appliedeverything.integration.jei;

import java.util.List;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.core.definitions.AEItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/** Loaded by JEI only when JEI is installed; the mod itself does not depend on JEI. */
@JeiPlugin
public class AppliedEverythingJeiPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(AppliedEverything.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CompressionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(CompressionCategory.RECIPE_TYPE, List.of(new CompressionRecipe(
                AEItems.SINGULARITY.stack(SingularityCompressorBlockEntity.SINGULARITIES_PER_OPERATION),
                new ItemStack(ModItems.COMPRESSED_SINGULARITY.get()),
                SingularityCompressorBlockEntity.ENERGY_PER_OPERATION)));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(CompressionCategory.RECIPE_TYPE, ModItems.SINGULARITY_COMPRESSOR.get());
    }
}
