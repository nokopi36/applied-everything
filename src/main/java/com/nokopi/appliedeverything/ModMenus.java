package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.machine.SingularityCompressorMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, AppliedEverything.MODID);

    static {
        MENU_TYPES.register("singularity_compressor", () -> SingularityCompressorMenu.TYPE);
    }

    private ModMenus() {
    }
}
