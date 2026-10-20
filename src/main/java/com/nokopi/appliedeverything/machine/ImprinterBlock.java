package com.nokopi.appliedeverything.machine;

import net.minecraft.world.inventory.MenuType;

public class ImprinterBlock extends FrameMachineBlock<ImprinterBlockEntity> {
    public ImprinterBlock(Properties props) {
        super(props);
    }

    @Override
    protected MenuType<?> getMenuType() {
        return ImprinterMenu.TYPE;
    }
}
