package com.nokopi.appliedeverything.machine;

import appeng.block.AEBaseEntityBlock;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class SingularityCompressorBlock extends AEBaseEntityBlock<SingularityCompressorBlockEntity> {
    /** The axis the open front and back face along. */
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    /** Whether a compression is in progress: the pyramids glow and a singularity floats in the middle. */
    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    public SingularityCompressorBlock(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.Z).setValue(WORKING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS, WORKING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // The open side faces the player who places it
        return super.getStateForPlacement(context).setValue(AXIS, context.getHorizontalDirection().getAxis());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        var be = getBlockEntity(level, pos);
        if (be == null) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
        if (!level.isClientSide()) {
            MenuOpener.open(SingularityCompressorMenu.TYPE, player, MenuLocators.forBlockEntity(be));
        }
        return InteractionResult.SUCCESS;
    }
}
