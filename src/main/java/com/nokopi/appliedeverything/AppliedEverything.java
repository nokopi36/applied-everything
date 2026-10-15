package com.nokopi.appliedeverything;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import com.nokopi.appliedeverything.cell.InfinityCellHandler;
import com.nokopi.appliedeverything.gametest.ModGameTests;

import appeng.api.storage.StorageCells;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(AppliedEverything.MODID)
public class AppliedEverything {
    public static final String MODID = "applied_everything";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AppliedEverything(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModGameTests.TEST_FUNCTIONS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        StorageCells.addCellHandler(InfinityCellHandler.INSTANCE);
    }
}
