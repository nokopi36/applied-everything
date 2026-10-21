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
        addBlock(ModBlocks.IMPRINTER, "Infinity Imprinter");
        add(CompressionCategory.ENERGY_KEY, "%s AE");
        addItem(ModItems.BLANK_INFINITY_FLUID_CELL, "Blank Infinity Fluid Cell");
        addItem(ModItems.INFINITY_FLUID_CELL, "Infinity Fluid Cell");
        // Written even without Applied Flux, so the language files do not depend on what was installed at datagen time
        add("item.applied_everything.infinity_energy_cell", "Infinity Energy Cell");
        add(InfinityEnergyCellItem.CHARGING_NAME_KEY, "%s (%s%%)");
        add(InfinityEnergyCellItem.TOOLTIP_CHARGE_KEY, "Charge: %s / %s FE");
        add(InfinityFluidCellItem.IMPRINTED_NAME_KEY, "Infinity Fluid Cell: %s");
        add(InfinityCellItem.IMPRINTED_NAME_KEY, "Infinity Cell: %s");
        add(InfinityCellItem.TOOLTIP_SUPPLY_KEY, "Infinite.");
        add(InfinityCellItem.TOOLTIP_BLANK_KEY, "Not imprinted");
        add(MachineTooltips.PROGRESS_KEY, "Progress: %s%%");
        add(MachineTooltips.IMPRINTING_KEY, "Imprinting: %s");
        add(MachineTooltips.CHARGE_KEY, "Charge: %s%%");
    }
}
