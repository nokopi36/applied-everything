package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModCreativeTabs;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModJaJpLanguageProvider extends LanguageProvider {
    public ModJaJpLanguageProvider(PackOutput output) {
        super(output, AppliedEverything.MODID, "ja_jp");
    }

    @Override
    protected void addTranslations() {
        add(ModCreativeTabs.TAB_TITLE_KEY, "Applied Everything");
    }
}
