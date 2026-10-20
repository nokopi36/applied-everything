package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModCreativeTabs;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellItem;
import com.nokopi.appliedeverything.cell.InfinityFluidCellItem;
import com.nokopi.appliedeverything.integration.jei.CompressionCategory;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AppliedEverything.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModCreativeTabs.TAB_TITLE_KEY, "Applied Everything");
        addItem(ModItems.COMPRESSED_SINGULARITY, "Compressed Singularity");
        addItem(ModItems.INFINITY_CORE, "Infinity Core");
        addItem(ModItems.BLANK_INFINITY_CELL, "Blank Infinity Cell");
        addItem(ModItems.INFINITY_CELL, "Infinity Cell");
        addBlock(ModBlocks.SINGULARITY_COMPRESSOR, "Singularity Compressor");
        addBlock(ModBlocks.IMPRINTER, "Imprinter");
        add(CompressionCategory.ENERGY_KEY, "%s AE");
        addItem(ModItems.BLANK_INFINITY_FLUID_CELL, "Blank Infinity Fluid Cell");
        addItem(ModItems.INFINITY_FLUID_CELL, "Infinity Fluid Cell");
        add(InfinityFluidCellItem.IMPRINTED_NAME_KEY, "Infinity Fluid Cell: %s");
        add(InfinityCellItem.IMPRINTED_NAME_KEY, "Infinity Cell: %s");
        add(InfinityCellItem.TOOLTIP_SUPPLY_KEY, "Unlimited supply");
        add(InfinityCellItem.TOOLTIP_VOID_KEY, "Inserted items are voided");
        add(InfinityCellItem.TOOLTIP_BLANK_KEY, "Not imprinted");
    }
}
