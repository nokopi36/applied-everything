package com.nokopi.appliedeverything.integration.appflux;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.nokopi.appliedeverything.ModDataComponents;
import com.nokopi.appliedeverything.cell.ImprintedCell;
import com.nokopi.appliedeverything.cell.InfinityCellItem;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.implementations.items.IAEItemPowerStorage;
import appeng.api.stacks.AEKey;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

/**
 * Charged with a billion FE, this cell supplies unlimited FE through Applied Flux. Registered only when Applied Flux
 * is installed.
 */
public class InfinityEnergyCellItem extends Item implements ImprintedCell, IAEItemPowerStorage {
    /** FE needed before the cell becomes infinite. */
    public static final int CAPACITY_FE = 1_000_000_000;
    /** AE2's default exchange rate: 1 AE = 2 FE. */
    public static final double FE_PER_AE = 2;
    /** AE per tick the cell takes from AE2's charger. */
    public static final double CHARGER_RATE_AE = 10_000;

    public static final String CHARGING_NAME_KEY = "item.applied_everything.infinity_energy_cell.charging";
    public static final String TOOLTIP_CHARGE_KEY = "tooltip.applied_everything.infinity_energy_cell.charge";

    public InfinityEnergyCellItem(Properties properties) {
        super(properties);
    }

    public static int getStoredFe(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.STORED_FE.get(), 0);
    }

    public static boolean isFullyCharged(ItemStack stack) {
        return getStoredFe(stack) >= CAPACITY_FE;
    }

    /** Adds FE to the cell and returns how much was taken. */
    public static int charge(ItemStack stack, long fe, boolean simulate) {
        int stored = getStoredFe(stack);
        int taken = (int) Math.min(fe, (long) CAPACITY_FE - stored);
        if (taken > 0 && !simulate) {
            stack.set(ModDataComponents.STORED_FE.get(), stored + taken);
        }
        return Math.max(taken, 0);
    }

    @Override
    @Nullable
    public AEKey getImprintedKey(ItemStack stack) {
        return isFullyCharged(stack) ? AppFluxKeys.fe() : null;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (isFullyCharged(stack)) {
            return super.getName(stack);
        }
        long percent = (long) getStoredFe(stack) * 100 / CAPACITY_FE;
        return Component.translatable(CHARGING_NAME_KEY, super.getName(stack), percent);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
            Consumer<Component> lines, TooltipFlag flag) {
        if (isFullyCharged(stack)) {
            lines.accept(Component.translatable(InfinityCellItem.TOOLTIP_SUPPLY_KEY).withStyle(ChatFormatting.GRAY));
            lines.accept(Component.translatable(InfinityCellItem.TOOLTIP_VOID_KEY).withStyle(ChatFormatting.GRAY));
            return;
        }
        lines.accept(Component.translatable(TOOLTIP_CHARGE_KEY, String.format("%,d", getStoredFe(stack)),
                String.format("%,d", CAPACITY_FE)).withStyle(ChatFormatting.YELLOW));
    }

    // --- AE2's charger -------------------------------------------------------------------------------

    @Override
    public double injectAEPower(ItemStack stack, double amount, Actionable mode) {
        int taken = charge(stack, (long) (amount * FE_PER_AE), mode == Actionable.SIMULATE);
        return Math.max(0, amount - taken / FE_PER_AE);
    }

    @Override
    public double extractAEPower(ItemStack stack, double amount, Actionable mode) {
        return 0;
    }

    @Override
    public double getAEMaxPower(ItemStack stack) {
        return CAPACITY_FE / FE_PER_AE;
    }

    @Override
    public double getAECurrentPower(ItemStack stack) {
        return getStoredFe(stack) / FE_PER_AE;
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack stack) {
        return isFullyCharged(stack) ? AccessRestriction.NO_ACCESS : AccessRestriction.WRITE;
    }

    @Override
    public double getChargeRate(ItemStack stack) {
        return CHARGER_RATE_AE;
    }
}
