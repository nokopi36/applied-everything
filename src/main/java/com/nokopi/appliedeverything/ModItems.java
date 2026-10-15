package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.cell.InfinityCellItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AppliedEverything.MODID);

    public static final DeferredItem<Item> COMPRESSED_SINGULARITY = ITEMS.registerSimpleItem("compressed_singularity",
            p -> p.rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> INFINITY_CORE = ITEMS.registerSimpleItem("infinity_core",
            p -> p.rarity(Rarity.RARE));
    public static final DeferredItem<Item> BLANK_INFINITY_CELL = ITEMS.registerSimpleItem("blank_infinity_cell",
            p -> p.stacksTo(1).rarity(Rarity.EPIC));
    public static final DeferredItem<InfinityCellItem> INFINITY_CELL = ITEMS.registerItem("infinity_cell",
            InfinityCellItem::new, p -> p.stacksTo(1).rarity(Rarity.EPIC));

    private ModItems() {
    }
}
