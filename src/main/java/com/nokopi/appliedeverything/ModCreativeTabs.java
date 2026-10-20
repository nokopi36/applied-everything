package com.nokopi.appliedeverything;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AppliedEverything.MODID);

    public static final String TAB_TITLE_KEY = "itemGroup." + AppliedEverything.MODID;

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable(TAB_TITLE_KEY))
            .icon(() -> ModItems.BLANK_INFINITY_CELL.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.COMPRESSED_SINGULARITY.get());
                output.accept(ModItems.INFINITY_CORE.get());
                output.accept(ModItems.BLANK_INFINITY_CELL.get());
                output.accept(ModItems.BLANK_INFINITY_FLUID_CELL.get());
                output.accept(ModItems.SINGULARITY_COMPRESSOR.get());
                output.accept(ModItems.IMPRINTER.get());
            })
            .build());

    private ModCreativeTabs() {
    }
}
