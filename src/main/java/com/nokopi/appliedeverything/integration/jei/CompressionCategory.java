package com.nokopi.appliedeverything.integration.jei;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModItems;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;

public class CompressionCategory implements IRecipeCategory<CompressionRecipe> {
    public static final IRecipeType<CompressionRecipe> RECIPE_TYPE = IRecipeType.create(AppliedEverything.MODID,
            "singularity_compression", CompressionRecipe.class);
    public static final String ENERGY_KEY = "jei.applied_everything.singularity_compression.energy";

    private static final int WIDTH = 116;
    private static final int HEIGHT = 36;

    private final IDrawable icon;

    public CompressionCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(ModItems.SINGULARITY_COMPRESSOR.get());
    }

    @Override
    public IRecipeType<CompressionRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(ModBlocks.SINGULARITY_COMPRESSOR.get().getDescriptionId());
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressionRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(18, 1).setStandardSlotBackground().add(recipe.input());
        builder.addOutputSlot(80, 1).setOutputSlotBackground().add(recipe.output());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CompressionRecipe recipe, IFocusGroup focuses) {
        builder.addRecipeArrowWidget().setPosition(45, 1);
        builder.addText(Component.translatable(ENERGY_KEY, String.format("%,.0f", recipe.energy())), WIDTH, 10)
                .setPosition(0, 25)
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setColor(0xFF808080);
    }
}
