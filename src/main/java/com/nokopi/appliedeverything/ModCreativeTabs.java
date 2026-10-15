package com.nokopi.appliedeverything;

import appeng.core.definitions.AEItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AppliedEverything.MODID);

    public static final String TAB_TITLE_KEY = "itemGroup." + AppliedEverything.MODID;

    // TODO: Replace the icon with the Infinity Cell once it exists
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable(TAB_TITLE_KEY))
            .icon(AEItems.SINGULARITY::stack)
            .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
            .build());

    private ModCreativeTabs() {
    }
}
