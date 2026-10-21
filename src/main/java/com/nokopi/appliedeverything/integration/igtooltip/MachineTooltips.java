package com.nokopi.appliedeverything.integration.igtooltip;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.machine.ImprinterBlock;
import com.nokopi.appliedeverything.machine.ImprinterBlockEntity;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlock;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.api.integrations.igtooltip.ClientRegistration;
import appeng.api.integrations.igtooltip.CommonRegistration;
import appeng.api.integrations.igtooltip.TooltipBuilder;
import appeng.api.integrations.igtooltip.TooltipContext;
import appeng.api.integrations.igtooltip.TooltipProvider;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * Machine lines for Jade, WTHIT and The One Probe, through AE2's in-game tooltip integration.
 * Loaded by AE2 with the service loader (META-INF/services), so it is only used when one of those mods is installed.
 * <p>
 * Progress is not synced to clients, so the server puts it into the tooltip data and the client only formats it.
 */
public final class MachineTooltips implements TooltipProvider {
    public static final String PROGRESS_KEY = "tooltip.applied_everything.machine.progress";
    public static final String IMPRINTING_KEY = "tooltip.applied_everything.machine.imprinting";
    public static final String CHARGE_KEY = "tooltip.applied_everything.machine.charge";

    // Shared with other providers in the same tag, hence the prefix
    public static final String TAG_PROGRESS = "applied_everything_progress";
    public static final String TAG_CHARGE = "applied_everything_charge";
    public static final String TAG_TARGET_ITEM = "applied_everything_target_item";
    public static final String TAG_TARGET_FLUID = "applied_everything_target_fluid";

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(AppliedEverything.MODID, path);
    }

    @Override
    public void registerCommon(CommonRegistration registration) {
        registration.addBlockEntityData(id("singularity_compressor"), SingularityCompressorBlockEntity.class,
                MachineTooltips::compressorData);
        registration.addBlockEntityData(id("imprinter"), ImprinterBlockEntity.class, MachineTooltips::imprinterData);
    }

    @Override
    public void registerClient(ClientRegistration registration) {
        registration.addBlockEntityBody(SingularityCompressorBlockEntity.class, SingularityCompressorBlock.class,
                id("singularity_compressor"), MachineTooltips::body);
        registration.addBlockEntityBody(ImprinterBlockEntity.class, ImprinterBlock.class, id("imprinter"),
                MachineTooltips::body);
    }

    public static void compressorData(Player player, SingularityCompressorBlockEntity machine, CompoundTag data) {
        if (machine.getConsumedEnergy() > 0) {
            data.putInt(TAG_PROGRESS, machine.getProgressPercent());
        }
    }

    public static void imprinterData(Player player, ImprinterBlockEntity machine, CompoundTag data) {
        if (machine.isChargingEnergyCell()) {
            data.putInt(TAG_CHARGE, machine.getProgressPercent());
            return;
        }
        var target = machine.getImprintTarget();
        if (target instanceof AEItemKey item) {
            data.putString(TAG_TARGET_ITEM, item.getId().toString());
        } else if (target instanceof AEFluidKey fluid) {
            data.putString(TAG_TARGET_FLUID, fluid.getId().toString());
        } else {
            return;
        }
        data.putInt(TAG_PROGRESS, machine.getProgressPercent());
    }

    private static void body(Object machine, TooltipContext context, TooltipBuilder tooltip) {
        var data = context.serverData();
        var target = targetName(data);
        if (target != null) {
            tooltip.addLine(Component.translatable(IMPRINTING_KEY, target).withStyle(ChatFormatting.GRAY));
        }
        if (data.contains(TAG_PROGRESS)) {
            tooltip.addLine(Component.translatable(PROGRESS_KEY, data.getIntOr(TAG_PROGRESS, 0))
                    .withStyle(ChatFormatting.GRAY));
        }
        if (data.contains(TAG_CHARGE)) {
            tooltip.addLine(Component.translatable(CHARGE_KEY, data.getIntOr(TAG_CHARGE, 0))
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    /** Imprint targets carry no components (see ImprintRules), so the registry id is enough to name them. */
    private static Component targetName(CompoundTag data) {
        var itemId = data.getStringOr(TAG_TARGET_ITEM, "");
        if (!itemId.isEmpty()) {
            return BuiltInRegistries.ITEM.getOptional(Identifier.parse(itemId))
                    .map(item -> AEItemKey.of(item).getDisplayName())
                    .orElse(null);
        }
        var fluidId = data.getStringOr(TAG_TARGET_FLUID, "");
        if (!fluidId.isEmpty()) {
            return BuiltInRegistries.FLUID.getOptional(Identifier.parse(fluidId))
                    .map(fluid -> AEFluidKey.of(fluid).getDisplayName())
                    .orElse(null);
        }
        return null;
    }
}
