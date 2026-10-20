package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.client.ImprinterScreen;
import com.nokopi.appliedeverything.client.FrameMachineRenderer;
import com.nokopi.appliedeverything.client.SingularityCompressorScreen;
import com.nokopi.appliedeverything.machine.ImprinterMenu;
import com.nokopi.appliedeverything.machine.SingularityCompressorMenu;

import appeng.api.client.StorageCellModels;
import appeng.core.definitions.AEItems;
import appeng.client.InitScreens;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InitializeClientRegistriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = AppliedEverything.MODID, dist = Dist.CLIENT)
public class AppliedEverythingClient {
    // Placeholder look inside ME drives: reuse AE2's creative cell model
    private static final Identifier DRIVE_CELL_MODEL = Identifier.parse("ae2:block/drive_creative_cell");

    public AppliedEverythingClient(IEventBus modEventBus) {
        modEventBus.addListener(this::initClientRegistries);
        modEventBus.addListener(this::registerScreens);
        modEventBus.addListener(this::registerRenderers);
    }

    private void initClientRegistries(InitializeClientRegistriesEvent event) {
        StorageCellModels.registerModel(ModItems.INFINITY_CELL.get(), DRIVE_CELL_MODEL);
        StorageCellModels.registerModel(ModItems.INFINITY_FLUID_CELL.get(), DRIVE_CELL_MODEL);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SINGULARITY_COMPRESSOR.get(),
                context -> new FrameMachineRenderer<>(context, be -> AEItems.SINGULARITY.stack()));
        event.registerBlockEntityRenderer(ModBlockEntities.IMPRINTER.get(),
                context -> new FrameMachineRenderer<>(context, be -> be.getCellSlot().getStackInSlot(0)));
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        // AE2 loads screen styles from its own namespace: assets/ae2/screens/...
        InitScreens.register(event, SingularityCompressorMenu.TYPE, SingularityCompressorScreen::new,
                "/screens/applied_everything/singularity_compressor.json");
        InitScreens.register(event, ImprinterMenu.TYPE, ImprinterScreen::new, "/screens/applied_everything/imprinter.json");
    }
}
