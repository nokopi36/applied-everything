package com.nokopi.appliedeverything.imprint;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityFluidCellItem;

import appeng.api.stacks.AEFluidKey;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Blank Infinity Fluid Cell + one filled fluid container (shapeless) = Infinity Fluid Cell of that fluid.
 * The container is handed back empty.
 */
public class FluidImprintRecipe extends CustomRecipe {
    public static final FluidImprintRecipe INSTANCE = new FluidImprintRecipe();
    public static final MapCodec<FluidImprintRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidImprintRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<FluidImprintRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    /** The container slot, the fluid it holds and what the container becomes once emptied. */
    private record Target(int slot, FluidResource fluid, ItemStack emptied) {
    }

    @Nullable
    private static Target findTarget(CraftingInput input) {
        if (input.ingredientCount() != 2) {
            return null;
        }
        boolean hasBlankCell = false;
        int containerSlot = -1;
        for (int i = 0; i < input.size(); i++) {
            var stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.BLANK_INFINITY_FLUID_CELL.get()) && !hasBlankCell) {
                hasBlankCell = true;
            } else if (containerSlot < 0) {
                containerSlot = i;
            } else {
                return null;
            }
        }
        if (!hasBlankCell || containerSlot < 0) {
            return null;
        }
        return drain(containerSlot, input.getItem(containerSlot));
    }

    /** Empties a copy of one container and reports the fluid it held, or null if it holds no imprintable fluid. */
    @Nullable
    private static Target drain(int slot, ItemStack container) {
        // A one-slot inventory, so containers that swap themselves (a water bucket becoming a bucket) can do so
        var holder = new ItemStacksResourceHandler(NonNullList.of(ItemStack.EMPTY, container.copyWithCount(1)));
        var access = ItemAccess.forHandlerIndex(holder, 0);
        var handler = access.getCapability(Capabilities.Fluid.ITEM);
        if (handler == null) {
            return null;
        }
        for (int i = 0; i < handler.size(); i++) {
            var fluid = handler.getResource(i);
            int amount = handler.getAmountAsInt(i);
            if (fluid.isEmpty() || amount <= 0 || !ImprintRules.canImprint(fluid)) {
                continue;
            }
            try (var tx = Transaction.openRoot()) {
                if (handler.extract(fluid, amount, tx) <= 0) {
                    continue;
                }
                tx.commit();
            }
            int left = holder.getAmountAsInt(0);
            var emptied = left > 0 ? holder.getResource(0).toStack(left) : ItemStack.EMPTY;
            return new Target(slot, fluid, emptied);
        }
        return null;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return findTarget(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        var target = findTarget(input);
        return target != null ? InfinityFluidCellItem.imprinted(AEFluidKey.of(target.fluid())) : ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        var remaining = CraftingRecipe.defaultCraftingReminder(input);
        var target = findTarget(input);
        if (target != null) {
            remaining.set(target.slot(), target.emptied());
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<FluidImprintRecipe> getSerializer() {
        return SERIALIZER;
    }
}
