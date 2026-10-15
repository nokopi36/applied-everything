package com.nokopi.appliedeverything;

import java.util.concurrent.atomic.AtomicReference;

import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.blockentity.AEBaseBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AppliedEverything.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SingularityCompressorBlockEntity>> SINGULARITY_COMPRESSOR = BLOCK_ENTITY_TYPES.register("singularity_compressor",
            ModBlockEntities::createSingularityCompressor);

    private ModBlockEntities() {
    }

    // AE2's block base class needs to be told about its block entity type and ticker, the same way AE2 does it
    private static BlockEntityType<SingularityCompressorBlockEntity> createSingularityCompressor() {
        var block = ModBlocks.SINGULARITY_COMPRESSOR.get();
        var holder = new AtomicReference<BlockEntityType<SingularityCompressorBlockEntity>>();
        var type = new BlockEntityType<>((pos, state) -> new SingularityCompressorBlockEntity(holder.get(), pos, state), block);
        holder.setPlain(type);
        AEBaseBlockEntity.registerBlockEntityItem(type, block.asItem());
        block.setBlockEntity(SingularityCompressorBlockEntity.class, type, null,
                (level, pos, state, be) -> be.serverTick());
        return type;
    }
}
