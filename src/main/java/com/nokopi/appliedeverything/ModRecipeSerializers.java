package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.imprint.ImprintRecipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AppliedEverything.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ImprintRecipe>> IMPRINT = RECIPE_SERIALIZERS.register("imprint",
            () -> ImprintRecipe.SERIALIZER);

    private ModRecipeSerializers() {
    }
}
