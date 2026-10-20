package com.nokopi.appliedeverything;

import java.util.function.Supplier;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AppliedEverything.MODID);

    private static final StreamCodec<RegistryFriendlyByteBuf, AEItemKey> ITEM_KEY_STREAM_CODEC = narrow(AEItemKey.class);
    private static final StreamCodec<RegistryFriendlyByteBuf, AEFluidKey> FLUID_KEY_STREAM_CODEC = narrow(AEFluidKey.class);

    /**
     * The item an Infinity Cell supplies. Set once when the cell is imprinted and never changed afterwards.
     */
    public static final Supplier<DataComponentType<AEItemKey>> IMPRINTED_ITEM = DATA_COMPONENTS.registerComponentType("imprinted_item",
            builder -> builder.persistent(AEItemKey.CODEC).networkSynchronized(ITEM_KEY_STREAM_CODEC));

    /** The fluid an Infinity Fluid Cell supplies. */
    public static final Supplier<DataComponentType<AEFluidKey>> IMPRINTED_FLUID = DATA_COMPONENTS.registerComponentType("imprinted_fluid",
            builder -> builder.persistent(AEFluidKey.CODEC).networkSynchronized(FLUID_KEY_STREAM_CODEC));

    private static <K extends AEKey> StreamCodec<RegistryFriendlyByteBuf, K> narrow(Class<K> keyClass) {
        return AEKey.STREAM_CODEC.map(key -> {
            if (keyClass.isInstance(key)) {
                return keyClass.cast(key);
            }
            throw new IllegalArgumentException("Expected " + keyClass.getSimpleName() + ", got " + key);
        }, key -> key);
    }

    private ModDataComponents() {
    }
}
