package com.nokopi.appliedeverything.imprint;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellItem;

import appeng.api.stacks.AEItemKey;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Blank Infinity Cell + any one imprintable item (shapeless) = Infinity Cell of that item.
 */
public class ImprintRecipe extends CustomRecipe {
    public static final ImprintRecipe INSTANCE = new ImprintRecipe();
    public static final MapCodec<ImprintRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ImprintRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<ImprintRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    /** Returns the item to imprint, or null if the grid is not exactly one blank cell and one imprintable item. */
    @Nullable
    private static ItemStack findTarget(CraftingInput input) {
        if (input.ingredientCount() != 2) {
            return null;
        }
        boolean hasBlankCell = false;
        ItemStack target = null;
        for (int i = 0; i < input.size(); i++) {
            var stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.BLANK_INFINITY_CELL.get()) && !hasBlankCell) {
                hasBlankCell = true;
            } else if (target == null) {
                target = stack;
            } else {
                return null;
            }
        }
        return hasBlankCell && target != null && ImprintRules.canImprint(target) ? target : null;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return findTarget(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        var target = findTarget(input);
        return target != null ? InfinityCellItem.imprinted(AEItemKey.of(target)) : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<ImprintRecipe> getSerializer() {
        return SERIALIZER;
    }
}
