package com.nokopi.appliedeverything.machine;

import net.minecraft.world.inventory.MenuType;

public class SingularityCompressorBlock extends FrameMachineBlock<SingularityCompressorBlockEntity> {
    public SingularityCompressorBlock(Properties props) {
        super(props);
    }

    @Override
    protected MenuType<?> getMenuType() {
        return SingularityCompressorMenu.TYPE;
    }
}
