package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModCreativeTabs;
import com.nokopi.appliedeverything.ModItems;

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
    }
}
