package com.nokopi.appliedeverything.cell;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.nokopi.appliedeverything.ModDataComponents;
import com.nokopi.appliedeverything.ModItems;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class InfinityCellItem extends Item implements ImprintedCell {
    public static final String IMPRINTED_NAME_KEY = "item.applied_everything.infinity_cell.imprinted";
    public static final String TOOLTIP_SUPPLY_KEY = "tooltip.applied_everything.infinity_cell.supply";
    public static final String TOOLTIP_VOID_KEY = "tooltip.applied_everything.infinity_cell.void";
    public static final String TOOLTIP_BLANK_KEY = "tooltip.applied_everything.infinity_cell.not_imprinted";

    public InfinityCellItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static AEItemKey getImprintedItem(ItemStack stack) {
        return stack.get(ModDataComponents.IMPRINTED_ITEM.get());
    }

    @Override
    @Nullable
    public AEKey getImprintedKey(ItemStack stack) {
        return getImprintedItem(stack);
    }

    public static ItemStack imprinted(AEItemKey key) {
        var stack = new ItemStack(ModItems.INFINITY_CELL.get());
        stack.set(ModDataComponents.IMPRINTED_ITEM.get(), key);
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        var key = getImprintedItem(stack);
        if (key == null) {
            return super.getName(stack);
        }
        return Component.translatable(IMPRINTED_NAME_KEY, key.getDisplayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
            Consumer<Component> lines, TooltipFlag flag) {
        if (getImprintedItem(stack) == null) {
            lines.accept(Component.translatable(TOOLTIP_BLANK_KEY).withStyle(ChatFormatting.RED));
            return;
        }
        lines.accept(Component.translatable(TOOLTIP_SUPPLY_KEY).withStyle(ChatFormatting.GRAY));
        lines.accept(Component.translatable(TOOLTIP_VOID_KEY).withStyle(ChatFormatting.GRAY));
    }
}
