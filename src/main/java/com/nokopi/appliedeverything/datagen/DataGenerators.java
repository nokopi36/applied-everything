package com.nokopi.appliedeverything.datagen;

import com.nokopi.appliedeverything.AppliedEverything;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = AppliedEverything.MODID)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(ModLanguageProvider::new);
        event.createProvider(ModJaJpLanguageProvider::new);
    }
}
