package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModCreativeTabs;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellItem;
import com.nokopi.appliedeverything.cell.InfinityFluidCellItem;
import com.nokopi.appliedeverything.integration.appflux.InfinityEnergyCellItem;
import com.nokopi.appliedeverything.integration.igtooltip.MachineTooltips;
import com.nokopi.appliedeverything.integration.jei.CompressionCategory;

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
        addBlock(ModBlocks.SINGULARITY_COMPRESSOR, "特異点圧縮機");
        addBlock(ModBlocks.IMPRINTER, "無限刻印機");
        add(CompressionCategory.ENERGY_KEY, "%s AE");
        addItem(ModItems.BLANK_INFINITY_FLUID_CELL, "空の無限流体セル");
        addItem(ModItems.INFINITY_FLUID_CELL, "無限流体セル");
        // Written even without Applied Flux, so the language files do not depend on what was installed at datagen time
        add("item.applied_everything.infinity_energy_cell", "無限エネルギーセル");
        add(InfinityEnergyCellItem.CHARGING_NAME_KEY, "%s（%s%%）");
        add(InfinityEnergyCellItem.TOOLTIP_CHARGE_KEY, "充電: %s / %s FE");
        add(InfinityFluidCellItem.IMPRINTED_NAME_KEY, "無限流体セル: %s");
        add(InfinityCellItem.IMPRINTED_NAME_KEY, "無限セル: %s");
        add(InfinityCellItem.TOOLTIP_SUPPLY_KEY, "無限。");
        add(InfinityCellItem.TOOLTIP_BLANK_KEY, "未刻印");
        add(MachineTooltips.PROGRESS_KEY, "進み具合: %s%%");
        add(MachineTooltips.IMPRINTING_KEY, "刻印中: %s");
        add(MachineTooltips.CHARGE_KEY, "充電: %s%%");
    }
}
