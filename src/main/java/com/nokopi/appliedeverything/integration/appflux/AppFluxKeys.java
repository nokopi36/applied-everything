package com.nokopi.appliedeverything.integration.appflux;

import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;

import appeng.api.stacks.AEKey;

/** The only place that touches Applied Flux classes; loaded only when Applied Flux is installed. */
final class AppFluxKeys {
    private AppFluxKeys() {
    }

    static AEKey fe() {
        return FluxKey.of(EnergyType.FE);
    }
}
