package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModCreativeTabs;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellItem;

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
        add(InfinityCellItem.IMPRINTED_NAME_KEY, "Infinity Cell: %s");
        add(InfinityCellItem.TOOLTIP_SUPPLY_KEY, "Unlimited supply");
        add(InfinityCellItem.TOOLTIP_VOID_KEY, "Inserted items are voided");
        add(InfinityCellItem.TOOLTIP_BLANK_KEY, "Not imprinted");
    }
}
