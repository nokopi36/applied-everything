package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModCreativeTabs;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AppliedEverything.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModCreativeTabs.TAB_TITLE_KEY, "Applied Everything");
    }
}
