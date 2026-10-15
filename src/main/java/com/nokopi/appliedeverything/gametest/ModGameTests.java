package com.nokopi.appliedeverything.gametest;

import java.util.function.Consumer;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellInventory;
import com.nokopi.appliedeverything.cell.InfinityCellItem;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.StorageCells;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = AppliedEverything.MODID)
public final class ModGameTests {
    public static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS = DeferredRegister.create(BuiltInRegistries.TEST_FUNCTION, AppliedEverything.MODID);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> INFINITY_CELL_STORAGE = TEST_FUNCTIONS.register("infinity_cell_storage",
            () -> ModGameTests::infinityCellStorage);

    private static final Identifier EMPTY_STRUCTURE = id("empty");

    private ModGameTests() {
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(AppliedEverything.MODID, path);
    }

    @SubscribeEvent
    static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(id("default"), new TestEnvironmentDefinition.AllOf());
        event.registerTest(id("infinity_cell_storage"),
                new FunctionGameTestInstance(INFINITY_CELL_STORAGE.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
    }

    static void infinityCellStorage(GameTestHelper helper) {
        var diamond = AEItemKey.of(Items.DIAMOND);
        var iron = AEItemKey.of(Items.IRON_INGOT);
        var source = IActionSource.empty();

        var cell = InfinityCellItem.imprinted(diamond);
        helper.assertTrue(StorageCells.isCellHandled(cell), "Imprinted cell should be handled by AE2");
        var inv = StorageCells.getCellInventory(cell, null);
        helper.assertTrue(inv != null, "Imprinted cell should have an inventory");

        // Extraction never runs out
        for (int i = 0; i < 3; i++) {
            helper.assertValueEqual(inv.extract(diamond, 1_000_000_000L, Actionable.MODULATE, source), 1_000_000_000L, "extracted diamonds");
        }
        helper.assertValueEqual(inv.getAvailableStacks().get(diamond), InfinityCellInventory.REPORTED_AMOUNT, "reported diamonds");

        // The imprinted item is voided, anything else is rejected
        helper.assertValueEqual(inv.insert(diamond, 64, Actionable.MODULATE, source), 64L, "inserted diamonds");
        helper.assertValueEqual(inv.insert(iron, 64, Actionable.MODULATE, source), 0L, "inserted iron");
        helper.assertValueEqual(inv.extract(iron, 1, Actionable.MODULATE, source), 0L, "extracted iron");
        helper.assertValueEqual(inv.getAvailableStacks().get(iron), 0L, "reported iron");

        // A cell without an imprint is not a cell
        var blank = new ItemStack(ModItems.INFINITY_CELL.get());
        helper.assertFalse(StorageCells.isCellHandled(blank), "Cell without imprint should not be handled");
        helper.assertFalse(inv.canFitInsideCell(), "Infinity cell should not fit inside other cells");

        helper.succeed();
    }
}
