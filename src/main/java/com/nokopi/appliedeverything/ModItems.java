package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.cell.InfinityCellItem;
import com.nokopi.appliedeverything.cell.InfinityFluidCellItem;
import com.nokopi.appliedeverything.integration.appflux.AppFluxIntegration;
import com.nokopi.appliedeverything.integration.appflux.InfinityEnergyCellItem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AppliedEverything.MODID);

    public static final DeferredItem<Item> COMPRESSED_SINGULARITY = ITEMS.registerSimpleItem("compressed_singularity",
            p -> p.rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> INFINITY_CORE = ITEMS.registerSimpleItem("infinity_core",
            p -> p.rarity(Rarity.RARE));
    // Both cells always shimmer, so they stand out from AE2's cells even at inventory size
    public static final DeferredItem<Item> BLANK_INFINITY_CELL = ITEMS.registerSimpleItem("blank_infinity_cell",
            p -> p.stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final DeferredItem<InfinityCellItem> INFINITY_CELL = ITEMS.registerItem("infinity_cell",
            InfinityCellItem::new, p -> p.stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final DeferredItem<Item> BLANK_INFINITY_FLUID_CELL = ITEMS.registerSimpleItem("blank_infinity_fluid_cell",
            p -> p.stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final DeferredItem<InfinityFluidCellItem> INFINITY_FLUID_CELL = ITEMS.registerItem("infinity_fluid_cell",
            InfinityFluidCellItem::new, p -> p.stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    /** Only exists when Applied Flux is installed. */
    @Nullable
    public static final DeferredItem<InfinityEnergyCellItem> INFINITY_ENERGY_CELL = AppFluxIntegration.isLoaded()
            ? ITEMS.registerItem("infinity_energy_cell", InfinityEnergyCellItem::new,
                    p -> p.stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            : null;
    public static final DeferredItem<BlockItem> SINGULARITY_COMPRESSOR = ITEMS.registerSimpleBlockItem("singularity_compressor", ModBlocks.SINGULARITY_COMPRESSOR);
    public static final DeferredItem<BlockItem> IMPRINTER = ITEMS.registerSimpleBlockItem("imprinter", ModBlocks.IMPRINTER);

    private ModItems() {
    }
}
