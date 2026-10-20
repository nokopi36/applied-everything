package com.nokopi.appliedeverything.cell;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.nokopi.appliedeverything.ModDataComponents;
import com.nokopi.appliedeverything.ModItems;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class InfinityFluidCellItem extends Item implements ImprintedCell {
    public static final String IMPRINTED_NAME_KEY = "item.applied_everything.infinity_fluid_cell.imprinted";

    public InfinityFluidCellItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static AEFluidKey getImprintedFluid(ItemStack stack) {
        return stack.get(ModDataComponents.IMPRINTED_FLUID.get());
    }

    public static ItemStack imprinted(AEFluidKey key) {
        var stack = new ItemStack(ModItems.INFINITY_FLUID_CELL.get());
        stack.set(ModDataComponents.IMPRINTED_FLUID.get(), key);
        return stack;
    }

    @Override
    @Nullable
    public AEKey getImprintedKey(ItemStack stack) {
        return getImprintedFluid(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        var key = getImprintedFluid(stack);
        if (key == null) {
            return super.getName(stack);
        }
        return Component.translatable(IMPRINTED_NAME_KEY, key.getDisplayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
            Consumer<Component> lines, TooltipFlag flag) {
        if (getImprintedFluid(stack) == null) {
            lines.accept(Component.translatable(InfinityCellItem.TOOLTIP_BLANK_KEY).withStyle(ChatFormatting.RED));
            return;
        }
        lines.accept(Component.translatable(InfinityCellItem.TOOLTIP_SUPPLY_KEY).withStyle(ChatFormatting.GRAY));
        lines.accept(Component.translatable(InfinityCellItem.TOOLTIP_VOID_KEY).withStyle(ChatFormatting.GRAY));
    }
}
