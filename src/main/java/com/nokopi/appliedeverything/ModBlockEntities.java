package com.nokopi.appliedeverything;

import java.util.concurrent.atomic.AtomicReference;

import com.nokopi.appliedeverything.machine.ImprinterBlockEntity;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.ServerTickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AppliedEverything.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SingularityCompressorBlockEntity>> SINGULARITY_COMPRESSOR = BLOCK_ENTITY_TYPES.register("singularity_compressor",
            () -> create(ModBlocks.SINGULARITY_COMPRESSOR.get(), SingularityCompressorBlockEntity.class, SingularityCompressorBlockEntity::new));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImprinterBlockEntity>> IMPRINTER = BLOCK_ENTITY_TYPES.register("imprinter",
            () -> create(ModBlocks.IMPRINTER.get(), ImprinterBlockEntity.class, ImprinterBlockEntity::new));

    private ModBlockEntities() {
    }

    @FunctionalInterface
    private interface Factory<T extends BlockEntity> {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }

    // AE2's block base class needs to be told about its block entity type and ticker, the same way AE2 does it
    private static <T extends AEBaseBlockEntity & ServerTickingBlockEntity> BlockEntityType<T> create(
            AEBaseEntityBlock<T> block, Class<T> entityClass, Factory<T> factory) {
        var holder = new AtomicReference<BlockEntityType<T>>();
        var type = new BlockEntityType<>((pos, state) -> factory.create(holder.get(), pos, state), block);
        holder.setPlain(type);
        AEBaseBlockEntity.registerBlockEntityItem(type, block.asItem());
        block.setBlockEntity(entityClass, type, null, (level, pos, state, be) -> be.serverTick());
        return type;
    }
}
