package com.nokopi.appliedeverything;

import appeng.api.client.StorageCellModels;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InitializeClientRegistriesEvent;

@Mod(value = AppliedEverything.MODID, dist = Dist.CLIENT)
public class AppliedEverythingClient {
    // Placeholder look inside ME drives: reuse AE2's creative cell model
    private static final Identifier DRIVE_CELL_MODEL = Identifier.parse("ae2:block/drive_creative_cell");

    public AppliedEverythingClient(IEventBus modEventBus) {
        modEventBus.addListener(this::initClientRegistries);
    }

    private void initClientRegistries(InitializeClientRegistriesEvent event) {
        StorageCellModels.registerModel(ModItems.INFINITY_CELL.get(), DRIVE_CELL_MODEL);
    }
}
