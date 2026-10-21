package com.nokopi.appliedeverything.integration.appflux;

import com.nokopi.appliedeverything.AppliedEverything;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public final class AppFluxIntegration {
    public static final String MOD_ID = "appflux";

    private AppFluxIntegration() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    /**
     * Turns on the bundled resource pack with the Infinity Energy Cell's guide pages, only when Applied Flux is
     * installed: without it the cell does not exist and the pages would show errors.
     */
    public static void addPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES || !isLoaded()) {
            return;
        }
        event.addPackFinders(Identifier.fromNamespaceAndPath(AppliedEverything.MODID, "resourcepacks/appflux_compat"),
                PackType.CLIENT_RESOURCES, Component.literal("Applied Everything: Applied Flux integration"),
                PackSource.BUILT_IN, true, Pack.Position.TOP);
    }
}
