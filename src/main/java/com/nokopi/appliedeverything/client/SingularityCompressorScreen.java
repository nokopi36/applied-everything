package com.nokopi.appliedeverything.client;

import com.nokopi.appliedeverything.machine.SingularityCompressorMenu;

import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ProgressBar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SingularityCompressorScreen extends UpgradeableScreen<SingularityCompressorMenu> {
    private final ProgressBar progressBar;

    public SingularityCompressorScreen(SingularityCompressorMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.progressBar = new ProgressBar(this.menu, style.getImage("progressBar"), ProgressBar.Direction.VERTICAL);
        widgets.add("progressBar", this.progressBar);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();
        int percent = this.menu.getCurrentProgress() * 100 / this.menu.getMaxProgress();
        this.progressBar.setFullMsg(Component.literal(percent + "%"));
    }
}
