package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.integration.appflux.InfinityEnergyCellItem;

import appeng.api.AECapabilities;
import appeng.api.networking.IInWorldGridNodeHost;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;

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

        var imprinter = ModBlockEntities.IMPRINTER.get();
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, imprinter, (be, context) -> (IInWorldGridNodeHost) be);
        event.registerBlockEntity(Capabilities.Energy.BLOCK, imprinter, (be, side) -> be.getEnergyStorage(side));
        event.registerBlockEntity(Capabilities.Item.BLOCK, imprinter, (be, side) -> be.getExposedItemHandler(side));
        if (ModItems.INFINITY_ENERGY_CELL != null) {
            // Other mods' chargers fill the uncharged cell with FE; nothing can be taken back out
            event.registerItem(Capabilities.Energy.ITEM, (stack, access) -> new ItemAccessEnergyHandler(access,
                    ModDataComponents.STORED_FE.get(), ModServerConfig.energyCellCapacity(), ModServerConfig.energyCellCapacity(), 0),
                    ModItems.INFINITY_ENERGY_CELL.get());
        }

        // AE2 turns this into a fluid handler, so pipes can fill the tank
        event.registerBlockEntity(AECapabilities.GENERIC_INTERNAL_INV, imprinter, (be, side) -> be.getTank());
    }
}
