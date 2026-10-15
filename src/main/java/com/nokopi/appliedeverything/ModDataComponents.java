package com.nokopi.appliedeverything;

import java.util.function.Supplier;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AppliedEverything.MODID);

    private static final StreamCodec<RegistryFriendlyByteBuf, AEItemKey> ITEM_KEY_STREAM_CODEC = AEKey.STREAM_CODEC.map(
            key -> {
                if (key instanceof AEItemKey itemKey) {
                    return itemKey;
                }
                throw new IllegalArgumentException("Expected an item key, got " + key);
            },
            key -> key);

    /**
     * The item an Infinity Cell supplies. Set once when the cell is imprinted and never changed afterwards.
     */
    public static final Supplier<DataComponentType<AEItemKey>> IMPRINTED_ITEM = DATA_COMPONENTS.registerComponentType("imprinted_item",
            builder -> builder.persistent(AEItemKey.CODEC).networkSynchronized(ITEM_KEY_STREAM_CODEC));

    private ModDataComponents() {
    }
}
