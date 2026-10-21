package com.nokopi.appliedeverything.integration.appflux;

import net.neoforged.fml.ModList;

public final class AppFluxIntegration {
    public static final String MOD_ID = "appflux";

    private AppFluxIntegration() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
