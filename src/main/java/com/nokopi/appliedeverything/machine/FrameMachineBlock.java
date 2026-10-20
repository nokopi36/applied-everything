package com.nokopi.appliedeverything.machine;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A machine shaped like an open frame (front and back open), facing the player who placed it, with a "working" state
 * that switches on its effects. Right-clicking opens its menu.
 */
public abstract class FrameMachineBlock<T extends AEBaseBlockEntity> extends AEBaseEntityBlock<T> {
    /** The axis the open front and back face along. */
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    /** Whether the machine is busy: its pyramids glow and the item being processed floats in the middle. */
    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    protected FrameMachineBlock(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.Z).setValue(WORKING, false));
    }

    protected abstract MenuType<?> getMenuType();

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
            MenuOpener.open(getMenuType(), player, MenuLocators.forBlockEntity(be));
        }
        return InteractionResult.SUCCESS;
    }

    /** Shows or hides the working effects of the machine at the given block entity's position. */
    public static void setWorking(AEBaseBlockEntity be, boolean working) {
        var level = be.getLevel();
        var state = be.getBlockState();
        if (level != null && state.hasProperty(WORKING) && state.getValue(WORKING) != working) {
            level.setBlock(be.getBlockPos(), state.setValue(WORKING, working), Block.UPDATE_CLIENTS);
        }
    }
}
