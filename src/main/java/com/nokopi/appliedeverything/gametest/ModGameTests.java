package com.nokopi.appliedeverything.gametest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellInventory;
import com.nokopi.appliedeverything.cell.InfinityCellItem;
import com.nokopi.appliedeverything.imprint.ImprintRecipe;
import com.nokopi.appliedeverything.imprint.ImprintRules;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.StorageCells;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = AppliedEverything.MODID)
public final class ModGameTests {
    public static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS = DeferredRegister.create(BuiltInRegistries.TEST_FUNCTION, AppliedEverything.MODID);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> INFINITY_CELL_STORAGE = TEST_FUNCTIONS.register("infinity_cell_storage",
            () -> ModGameTests::infinityCellStorage);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> IMPRINT_RULES = TEST_FUNCTIONS.register("imprint_rules",
            () -> ModGameTests::imprintRules);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> IMPRINT_RECIPE = TEST_FUNCTIONS.register("imprint_recipe",
            () -> ModGameTests::imprintRecipe);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> COMPRESSOR_POWER = TEST_FUNCTIONS.register("compressor_power",
            () -> ModGameTests::compressorPower);

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
        event.registerTest(id("imprint_rules"),
                new FunctionGameTestInstance(IMPRINT_RULES.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("imprint_recipe"),
                new FunctionGameTestInstance(IMPRINT_RECIPE.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("compressor_power"),
                new FunctionGameTestInstance(COMPRESSOR_POWER.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
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

    static void imprintRules(GameTestHelper helper) {
        for (var item : List.of(Items.COBBLESTONE, Items.NETHER_STAR, Items.SHULKER_BOX, Items.DIAMOND_PICKAXE)) {
            helper.assertTrue(ImprintRules.canImprint(new ItemStack(item)), "Should be imprintable: " + item);
        }

        var renamed = new ItemStack(Items.COBBLESTONE);
        renamed.set(DataComponents.CUSTOM_NAME, Component.literal("Renamed"));
        var damaged = new ItemStack(Items.DIAMOND_PICKAXE);
        damaged.setDamageValue(10);
        var enchanted = new ItemStack(Items.DIAMOND_SWORD);
        enchanted.enchant(helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS), 1);
        var filledShulker = new ItemStack(Items.SHULKER_BOX);
        filledShulker.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(new ItemStack(Items.NETHER_STAR, 64))));

        var rejected = List.of(ItemStack.EMPTY, new ItemStack(ModItems.INFINITY_CORE.get()), new ItemStack(ModItems.BLANK_INFINITY_CELL.get()),
                InfinityCellItem.imprinted(AEItemKey.of(Items.DIAMOND)), renamed, damaged, enchanted, filledShulker);
        for (var stack : rejected) {
            helper.assertFalse(ImprintRules.canImprint(stack), "Should not be imprintable: " + stack);
        }

        helper.succeed();
    }

    static void imprintRecipe(GameTestHelper helper) {
        var level = helper.getLevel();
        var recipe = ImprintRecipe.INSTANCE;
        var blank = new ItemStack(ModItems.BLANK_INFINITY_CELL.get());
        var diamond = new ItemStack(Items.DIAMOND);

        // Shapeless: any positions in the grid
        var input = grid(ItemStack.EMPTY, diamond, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, blank, ItemStack.EMPTY, ItemStack.EMPTY);
        helper.assertTrue(recipe.matches(input, level), "Blank cell + diamond should match");
        var result = recipe.assemble(input);
        helper.assertTrue(result.is(ModItems.INFINITY_CELL.get()), "Result should be an Infinity Cell");
        helper.assertValueEqual(InfinityCellItem.getImprintedItem(result), AEItemKey.of(Items.DIAMOND), "imprinted item");

        helper.assertFalse(recipe.matches(grid(blank, diamond, new ItemStack(Items.COBBLESTONE)), level), "Three items should not match");
        helper.assertFalse(recipe.matches(grid(blank, blank.copy()), level), "Two blank cells should not match");
        helper.assertFalse(recipe.matches(grid(diamond), level), "Diamond alone should not match");
        helper.assertFalse(recipe.matches(grid(blank, new ItemStack(ModItems.INFINITY_CORE.get())), level), "Own items should not match");

        helper.succeed();
    }

    static void compressorPower(GameTestHelper helper) {
        var pos = new BlockPos(0, 1, 0);
        helper.setBlock(pos, ModBlocks.SINGULARITY_COMPRESSOR.get());
        var be = helper.getBlockEntity(pos, SingularityCompressorBlockEntity.class);
        helper.assertValueEqual(be.getInternalMaxPower(), SingularityCompressorBlockEntity.BUFFER_SIZE, "buffer size");

        be.injectAEPower(5_000, Actionable.MODULATE);
        helper.assertValueEqual(be.getInternalCurrentPower(), 5_000.0, "AE after injecting 5,000 AE");

        // FE arrives through the energy capability, converted at AE2's default rate of 2 FE = 1 AE
        var energy = helper.getLevel().getCapability(Capabilities.Energy.BLOCK, helper.absolutePos(pos), Direction.UP);
        helper.assertTrue(energy != null, "Compressor should expose an energy handler");
        try (var tx = Transaction.openRoot()) {
            helper.assertValueEqual(energy.insert(4_000, tx), 4_000, "FE accepted");
            tx.commit();
        }
        helper.assertValueEqual(be.getInternalCurrentPower(), 7_000.0, "AE after injecting 4,000 FE");

        // The buffer never exceeds its size
        be.injectAEPower(1_000_000, Actionable.MODULATE);
        helper.assertValueEqual(be.getInternalCurrentPower(), SingularityCompressorBlockEntity.BUFFER_SIZE, "AE after overfilling");

        helper.succeed();
    }

    private static CraftingInput grid(ItemStack... stacks) {
        var items = new ArrayList<>(List.of(stacks));
        while (items.size() < 9) {
            items.add(ItemStack.EMPTY);
        }
        return CraftingInput.of(3, 3, items);
    }
}
