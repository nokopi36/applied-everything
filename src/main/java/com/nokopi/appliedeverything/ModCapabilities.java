package com.nokopi.appliedeverything;

import appeng.api.AECapabilities;
import appeng.api.networking.IInWorldGridNodeHost;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = AppliedEverything.MODID)
public final class ModCapabilities {
    private ModCapabilities() {
    }

    @SubscribeEvent
    static void register(RegisterCapabilitiesEvent event) {
        var compressor = ModBlockEntities.SINGULARITY_COMPRESSOR.get();
        // Lets AE2 cables connect to the machine
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, compressor, (be, context) -> (IInWorldGridNodeHost) be);
        // Accepts FE from other mods' cables
        event.registerBlockEntity(Capabilities.Energy.BLOCK, compressor, (be, side) -> be.getEnergyStorage(side));
        // Hoppers, pipes and AE2 buses: insert singularities, extract compressed singularities
        event.registerBlockEntity(Capabilities.Item.BLOCK, compressor, (be, side) -> be.getExposedItemHandler(side));
    }
}
