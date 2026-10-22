package com.nokopi.appliedeverything.machine;

import com.nokopi.appliedeverything.ModServerConfig;
import com.nokopi.appliedeverything.AppliedEverything;

import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.UpgradeableMenu;
import appeng.menu.interfaces.IProgressProvider;
import appeng.menu.slot.AppEngSlot;
import appeng.menu.slot.OutputSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class SingularityCompressorMenu extends UpgradeableMenu<SingularityCompressorBlockEntity> implements IProgressProvider {
    public static final MenuType<SingularityCompressorMenu> TYPE = MenuTypeBuilder
            .create(SingularityCompressorMenu::new, SingularityCompressorBlockEntity.class)
            .buildUnregistered(Identifier.fromNamespaceAndPath(AppliedEverything.MODID, "singularity_compressor"));

    /** The progress bar works in units of this many AE so the values fit in an int. */
    private static final int PROGRESS_UNIT = 1_000;

    @GuiSync(2)
    public double consumedEnergy;

    public SingularityCompressorMenu(int id, Inventory playerInventory, SingularityCompressorBlockEntity host) {
        super(TYPE, id, playerInventory, host);
        this.addSlot(new AppEngSlot(host.getInput(), 0), SlotSemantics.MACHINE_INPUT);
        this.addSlot(new OutputSlot(host.getOutput(), 0, null), SlotSemantics.MACHINE_OUTPUT);
    }

    @Override
    protected void standardDetectAndSendChanges() {
        if (isServerSide()) {
            this.consumedEnergy = getHost().getConsumedEnergy();
        }
        super.standardDetectAndSendChanges();
    }

    @Override
    public int getCurrentProgress() {
        return (int) (consumedEnergy / PROGRESS_UNIT);
    }

    @Override
    public int getMaxProgress() {
        return (int) (ModServerConfig.energyPerCompression() / PROGRESS_UNIT);
    }
}
