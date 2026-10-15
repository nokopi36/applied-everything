package com.nokopi.appliedeverything.machine;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.inventories.InternalInventory;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.blockentity.grid.AENetworkedPoweredBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SingularityCompressorBlockEntity extends AENetworkedPoweredBlockEntity implements ServerTickingBlockEntity {
    /** AE consumed by one compression. */
    public static final double ENERGY_PER_OPERATION = 10_000_000;
    /** AE drawn per tick without acceleration cards. */
    public static final double BASE_DRAW_PER_TICK = 5_000;
    /** Additional AE drawn per tick for each installed acceleration card. */
    public static final double DRAW_PER_CARD = 5_000;
    public static final int MAX_ACCELERATION_CARDS = 4;
    /** Internal buffer: two ticks of the base draw, so large amounts of power cannot be stockpiled. */
    public static final double BUFFER_SIZE = BASE_DRAW_PER_TICK * 2;

    public SingularityCompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.getMainNode().setIdlePowerUsage(0);
        this.setInternalMaxPower(BUFFER_SIZE);
    }

    @Override
    public InternalInventory getInternalInventory() {
        return InternalInventory.empty();
    }

    @Override
    public void serverTick() {
        // AE2 networks do not charge machine buffers on their own, so pull what is missing from the grid
        var missing = getInternalMaxPower() - getInternalCurrentPower();
        if (missing <= 0.01) {
            return;
        }
        var grid = getMainNode().getGrid();
        if (grid == null) {
            return;
        }
        var extracted = grid.getEnergyService().extractAEPower(missing, Actionable.MODULATE, PowerMultiplier.ONE);
        if (extracted > 0) {
            injectAEPower(extracted, Actionable.MODULATE);
        }
    }
}
