package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModCreativeTabs;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellItem;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModJaJpLanguageProvider extends LanguageProvider {
    public ModJaJpLanguageProvider(PackOutput output) {
        super(output, AppliedEverything.MODID, "ja_jp");
    }

    @Override
    protected void addTranslations() {
        add(ModCreativeTabs.TAB_TITLE_KEY, "Applied Everything");
        addItem(ModItems.COMPRESSED_SINGULARITY, "圧縮特異点");
        addItem(ModItems.INFINITY_CORE, "無限コア");
        addItem(ModItems.BLANK_INFINITY_CELL, "空の無限セル");
        addItem(ModItems.INFINITY_CELL, "無限セル");
        add(InfinityCellItem.IMPRINTED_NAME_KEY, "無限セル: %s");
        add(InfinityCellItem.TOOLTIP_SUPPLY_KEY, "いくらでも取り出せます");
        add(InfinityCellItem.TOOLTIP_VOID_KEY, "入れたアイテムは消滅します");
        add(InfinityCellItem.TOOLTIP_BLANK_KEY, "未刻印");
    }
}
