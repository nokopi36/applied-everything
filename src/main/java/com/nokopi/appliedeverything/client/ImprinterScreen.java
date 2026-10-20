package com.nokopi.appliedeverything.client;

import com.nokopi.appliedeverything.machine.ImprinterMenu;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ProgressBar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ImprinterScreen extends AEBaseScreen<ImprinterMenu> {
    private final ProgressBar progressBar;

    public ImprinterScreen(ImprinterMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.progressBar = new ProgressBar(this.menu, style.getImage("progressBar"), ProgressBar.Direction.VERTICAL);
        widgets.add("progressBar", this.progressBar);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();
        this.progressBar.setFullMsg(Component.literal(this.menu.getCurrentProgress() + "%"));
    }
}
