package com.nokopi.appliedeverything.machine;

import com.nokopi.appliedeverything.AppliedEverything;

import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantic;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.interfaces.IProgressProvider;
import appeng.menu.slot.AppEngSlot;
import appeng.menu.slot.OutputSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ImprinterMenu extends AEBaseMenu implements IProgressProvider {
    public static final MenuType<ImprinterMenu> TYPE = MenuTypeBuilder
            .create(ImprinterMenu::new, ImprinterBlockEntity.class)
            .buildUnregistered(Identifier.fromNamespaceAndPath(AppliedEverything.MODID, "imprinter"));

    // Slot positions are looked up by these names in the screen layout (assets/ae2/screens/applied_everything/)
    public static final SlotSemantic CELL = SlotSemantics.register("APPLIED_EVERYTHING_IMPRINT_CELL", false);
    public static final SlotSemantic ITEM = SlotSemantics.register("APPLIED_EVERYTHING_IMPRINT_ITEM", false);
    public static final SlotSemantic CONTAINER = SlotSemantics.register("APPLIED_EVERYTHING_IMPRINT_CONTAINER", false);
    public static final SlotSemantic TANK = SlotSemantics.register("APPLIED_EVERYTHING_IMPRINT_TANK", false);

    private final ImprinterBlockEntity host;

    @GuiSync(0)
    public double consumedEnergy;

    public ImprinterMenu(int id, Inventory playerInventory, ImprinterBlockEntity host) {
        super(TYPE, id, playerInventory, host);
        this.host = host;
        this.addSlot(new AppEngSlot(host.getCellSlot(), 0), CELL);
        this.addSlot(new AppEngSlot(host.getItemSlot(), 0), ITEM);
        this.addSlot(new AppEngSlot(host.getContainerSlot(), 0), CONTAINER);
        this.addSlot(new AppEngSlot(host.getTank().createMenuWrapper(), 0), TANK);
        this.addSlot(new OutputSlot(host.getOutputSlot(), 0, null), SlotSemantics.MACHINE_OUTPUT);
        createPlayerInventorySlots(playerInventory);
    }

    @Override
    public void broadcastChanges() {
        if (isServerSide()) {
            this.consumedEnergy = host.getConsumedEnergy();
        }
        super.broadcastChanges();
    }

    @Override
    public int getCurrentProgress() {
        return (int) (consumedEnergy * 100 / ImprinterBlockEntity.ENERGY_PER_IMPRINT);
    }

    @Override
    public int getMaxProgress() {
        return 100;
    }
}
