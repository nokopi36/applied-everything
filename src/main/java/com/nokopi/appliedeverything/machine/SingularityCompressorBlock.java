package com.nokopi.appliedeverything.machine;

import appeng.block.AEBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SingularityCompressorBlock extends AEBaseEntityBlock<SingularityCompressorBlockEntity> {
    public static final String STORED_ENERGY_KEY = "message.applied_everything.singularity_compressor.stored";

    public SingularityCompressorBlock(Properties props) {
        super(props);
    }

    // TODO: Replace with the GUI in step (b)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        var be = getBlockEntity(level, pos);
        if (be == null) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
        if (!level.isClientSide()) {
            player.sendSystemMessage(Component.translatable(STORED_ENERGY_KEY,
                    String.format("%,.0f", be.getInternalCurrentPower()),
                    String.format("%,.0f", be.getInternalMaxPower())));
        }
        return InteractionResult.SUCCESS;
    }
}
