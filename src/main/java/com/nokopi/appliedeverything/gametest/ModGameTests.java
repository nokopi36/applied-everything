package com.nokopi.appliedeverything.gametest;

import com.nokopi.appliedeverything.ModServerConfig;
import java.util.List;
import java.util.function.Consumer;

import com.nokopi.appliedeverything.AppliedEverything;
import com.nokopi.appliedeverything.ModBlocks;
import com.nokopi.appliedeverything.ModItems;
import com.nokopi.appliedeverything.cell.InfinityCellInventory;
import com.nokopi.appliedeverything.cell.InfinityCellItem;
import com.nokopi.appliedeverything.cell.InfinityFluidCellItem;
import com.nokopi.appliedeverything.imprint.ImprintRules;
import com.nokopi.appliedeverything.integration.appflux.InfinityEnergyCellItem;
import com.nokopi.appliedeverything.integration.igtooltip.MachineTooltips;
import com.nokopi.appliedeverything.ModDataComponents;
import com.nokopi.appliedeverything.machine.FrameMachineBlock;
import com.nokopi.appliedeverything.machine.ImprinterBlockEntity;
import com.nokopi.appliedeverything.machine.ImprintTank;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.StorageCells;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
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
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> IMPRINTER = TEST_FUNCTIONS.register("imprinter",
            () -> ModGameTests::imprinter);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> IMPRINTER_TICKING = TEST_FUNCTIONS.register("imprinter_ticking",
            () -> ModGameTests::imprinterTicking);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> COMPRESSOR_POWER = TEST_FUNCTIONS.register("compressor_power",
            () -> ModGameTests::compressorPower);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> COMPRESSOR_PROCESSING = TEST_FUNCTIONS.register("compressor_processing",
            () -> ModGameTests::compressorProcessing);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> COMPRESSOR_AUTOMATION = TEST_FUNCTIONS.register("compressor_automation",
            () -> ModGameTests::compressorAutomation);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FLUID_CELL = TEST_FUNCTIONS.register("fluid_cell",
            () -> ModGameTests::fluidCell);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ENERGY_CELL = TEST_FUNCTIONS.register("energy_cell",
            () -> ModGameTests::energyCell);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MACHINE_TOOLTIPS = TEST_FUNCTIONS.register("machine_tooltips",
            () -> ModGameTests::machineTooltips);

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
        event.registerTest(id("imprinter"),
                new FunctionGameTestInstance(IMPRINTER.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("imprinter_ticking"),
                new FunctionGameTestInstance(IMPRINTER_TICKING.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 300, 0, true)));
        event.registerTest(id("compressor_power"),
                new FunctionGameTestInstance(COMPRESSOR_POWER.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("compressor_processing"),
                new FunctionGameTestInstance(COMPRESSOR_PROCESSING.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("compressor_automation"),
                new FunctionGameTestInstance(COMPRESSOR_AUTOMATION.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("fluid_cell"),
                new FunctionGameTestInstance(FLUID_CELL.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        event.registerTest(id("machine_tooltips"),
                new FunctionGameTestInstance(MACHINE_TOOLTIPS.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        if (ModItems.INFINITY_ENERGY_CELL != null) {
            event.registerTest(id("energy_cell"),
                    new FunctionGameTestInstance(ENERGY_CELL.getKey(), new TestData<>(environment, EMPTY_STRUCTURE, 20, 0, true)));
        }
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
                InfinityCellItem.imprinted(AEItemKey.of(Items.DIAMOND)), new ItemStack(ModBlocks.SINGULARITY_COMPRESSOR.get()),
                AEItems.SINGULARITY.stack(), renamed, damaged, enchanted, filledShulker);
        for (var stack : rejected) {
            helper.assertFalse(ImprintRules.canImprint(stack), "Should not be imprintable: " + stack);
        }

        helper.succeed();
    }

    static void imprinter(GameTestHelper helper) {
        // Items: a blank cell and cobblestone go in through the automation side, each into its own slot
        var itemPos = new BlockPos(0, 1, 0);
        helper.setBlock(itemPos, ModBlocks.IMPRINTER.get());
        var be = helper.getBlockEntity(itemPos, ImprinterBlockEntity.class);
        var items = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(itemPos), Direction.UP);
        try (var tx = Transaction.openRoot()) {
            helper.assertValueEqual(items.insert(ItemResource.of(ModItems.BLANK_INFINITY_CELL.get()), 1, tx), 1, "blank cells inserted");
            helper.assertValueEqual(items.insert(ItemResource.of(Items.COBBLESTONE), 3, tx), 3, "cobblestone inserted");
            helper.assertValueEqual(items.insert(ItemResource.of(ModItems.INFINITY_CORE.get()), 1, tx), 0, "infinity cores inserted");
            tx.commit();
        }
        helper.assertTrue(be.getCellSlot().getStackInSlot(0).is(ModItems.BLANK_INFINITY_CELL.get()), "Blank cell should be in the cell slot");
        helper.assertValueEqual(be.getItemSlot().getStackInSlot(0).getCount(), 3, "cobblestone in the item slot");

        helper.assertValueEqual(ticksToImprint(be), ModServerConfig.ticksPerImprint(), "ticks to imprint an item");
        var cell = be.getOutputSlot().getStackInSlot(0);
        helper.assertValueEqual(InfinityCellItem.getImprintedItem(cell), AEItemKey.of(Items.COBBLESTONE), "imprinted item");
        helper.assertValueEqual(be.getItemSlot().getStackInSlot(0).getCount(), 2, "cobblestone left");
        helper.assertTrue(be.getCellSlot().getStackInSlot(0).isEmpty(), "The blank cell should be used up");

        // Fluids: a water bucket is poured into the tank and stays behind empty
        var fluidPos = new BlockPos(2, 1, 0);
        helper.setBlock(fluidPos, ModBlocks.IMPRINTER.get());
        var fluidBe = helper.getBlockEntity(fluidPos, ImprinterBlockEntity.class);
        fluidBe.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_FLUID_CELL.get()));
        fluidBe.getContainerSlot().setItemDirect(0, new ItemStack(Items.WATER_BUCKET));
        fluidBe.serverTick();
        helper.assertValueEqual(fluidBe.getTank().getAmount(0), (long) ImprintTank.CAPACITY, "water in the tank");
        helper.assertTrue(fluidBe.getContainerSlot().getStackInSlot(0).is(Items.BUCKET), "The bucket should be left empty");

        helper.assertTrue(ticksToImprint(fluidBe) > 0, "Fluid cell should be imprinted");
        helper.assertValueEqual(InfinityFluidCellItem.getImprintedFluid(fluidBe.getOutputSlot().getStackInSlot(0)),
                AEFluidKey.of(Fluids.WATER), "imprinted fluid");
        helper.assertTrue(fluidBe.getTank().isEmpty(), "The tank should be used up");

        // Inputs that do not fit the waiting blank cell are refused
        var mixPos = new BlockPos(5, 1, 0);
        helper.setBlock(mixPos, ModBlocks.IMPRINTER.get());
        var mixBe = helper.getBlockEntity(mixPos, ImprinterBlockEntity.class);
        var water = AEFluidKey.of(Fluids.WATER);
        mixBe.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_FLUID_CELL.get()));
        helper.assertFalse(mixBe.getItemSlot().insertItem(0, new ItemStack(Items.COBBLESTONE), false).isEmpty(),
                "Items should be refused while a fluid cell waits");
        mixBe.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_CELL.get()));
        helper.assertValueEqual(mixBe.getTank().insert(0, water, 1000, Actionable.MODULATE), 0L, "water accepted while an item cell waits");
        helper.assertFalse(mixBe.getContainerSlot().insertItem(0, new ItemStack(Items.WATER_BUCKET), false).isEmpty(),
                "Containers should be refused while an item cell waits");
        mixBe.getCellSlot().setItemDirect(0, ItemStack.EMPTY);
        helper.assertTrue(mixBe.getItemSlot().insertItem(0, new ItemStack(Items.COBBLESTONE), false).isEmpty(),
                "Items should be accepted with no cell");
        helper.assertValueEqual(mixBe.getTank().insert(0, water, 1000, Actionable.MODULATE), 1000L, "water accepted with no cell");

        helper.succeed();
    }

    /** The imprinter works on its own as the world ticks: only power is supplied by the test. */
    static void imprinterTicking(GameTestHelper helper) {
        var pos = new BlockPos(0, 1, 0);
        helper.setBlock(pos, ModBlocks.IMPRINTER.get());
        var be = helper.getBlockEntity(pos, ImprinterBlockEntity.class);
        be.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_CELL.get()));
        be.getItemSlot().setItemDirect(0, new ItemStack(Items.COBBLESTONE));
        helper.onEachTick(() -> be.injectAEPower(be.getInternalMaxPower(), Actionable.MODULATE));

        // A second imprinter powered by an AE2 network: a creative energy cell right next to it
        var netPos = new BlockPos(3, 1, 0);
        helper.setBlock(netPos, ModBlocks.IMPRINTER.get());
        helper.setBlock(new BlockPos(4, 1, 0), AEBlocks.CREATIVE_ENERGY_CELL.block());
        var netBe = helper.getBlockEntity(netPos, ImprinterBlockEntity.class);
        netBe.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_CELL.get()));
        netBe.getItemSlot().setItemDirect(0, new ItemStack(Items.COBBLESTONE));

        helper.succeedWhen(() -> {
            helper.assertFalse(be.getOutputSlot().getStackInSlot(0).isEmpty(), "No imprinted cell yet (buffer power)");
            helper.assertFalse(netBe.getOutputSlot().getStackInSlot(0).isEmpty(),
                    "No imprinted cell yet (network power), grid=" + netBe.getMainNode().getGrid() + " consumed=" + netBe.getConsumedEnergy());
        });
    }

    /** Runs only when Applied Flux is installed (it is in the dev environment). */
    static void energyCell(GameTestHelper helper) {
        var item = ModItems.INFINITY_ENERGY_CELL.get();
        var source = IActionSource.empty();

        // AE2's charger interface: 1 AE = 2 FE
        var cell = new ItemStack(item);
        helper.assertFalse(StorageCells.isCellHandled(cell), "An uncharged cell should not work as a cell");
        helper.assertValueEqual(item.injectAEPower(cell, 1_000, Actionable.MODULATE), 0.0, "AE left over");
        helper.assertValueEqual(InfinityEnergyCellItem.getStoredFe(cell), 2_000, "FE after 1,000 AE");

        // Other mods' chargers: the FE capability takes everything and gives nothing back
        var holder = new net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler(net.minecraft.core.NonNullList.of(ItemStack.EMPTY, cell));
        var energy = ItemAccess.forHandlerIndex(holder, 0).getCapability(Capabilities.Energy.ITEM);
        helper.assertTrue(energy != null, "The cell should accept FE");
        try (var tx = Transaction.openRoot()) {
            helper.assertValueEqual(energy.insert(ModServerConfig.energyCellCapacity(), tx), ModServerConfig.energyCellCapacity() - 2_000, "FE accepted");
            helper.assertValueEqual(energy.extract(1_000, tx), 0, "FE extracted");
            tx.commit();
        }
        var charged = holder.getResource(0).toStack(1);
        helper.assertTrue(InfinityEnergyCellItem.isFullyCharged(charged), "The cell should be full");

        // Once full it supplies unlimited FE through Applied Flux
        var inv = StorageCells.getCellInventory(charged, null);
        helper.assertTrue(inv != null, "A charged cell should work as a cell");
        var fe = item.getImprintedKey(charged);
        helper.assertValueEqual(inv.extract(fe, 1_000_000_000_000L, Actionable.MODULATE, source), 1_000_000_000_000L, "FE extracted from the cell");

        // The imprinter charges an uncharged cell and moves it to the output once full
        var pos = new BlockPos(0, 1, 0);
        helper.setBlock(pos, ModBlocks.IMPRINTER.get());
        var be = helper.getBlockEntity(pos, ImprinterBlockEntity.class);
        var almostFull = new ItemStack(item);
        almostFull.set(ModDataComponents.STORED_FE.get(), ModServerConfig.energyCellCapacity() - 1_000);
        helper.assertTrue(be.getCellSlot().insertItem(0, almostFull, false).isEmpty(), "Uncharged cells go into the cell slot");
        helper.assertValueEqual(ticksToImprint(be) > 0, true, "The imprinter should finish charging");
        helper.assertTrue(InfinityEnergyCellItem.isFullyCharged(be.getOutputSlot().getStackInSlot(0)), "The output should be full");
        helper.assertFalse(be.getCellSlot().insertItem(0, charged, false).isEmpty(), "Full cells are refused");

        helper.succeed();
    }

    static void machineTooltips(GameTestHelper helper) {
        // Compressor: nothing while idle, the progress while compressing (400 ticks x 5,000 AE = 20 %)
        var compressorPos = new BlockPos(0, 1, 0);
        helper.setBlock(compressorPos, ModBlocks.SINGULARITY_COMPRESSOR.get());
        var compressor = helper.getBlockEntity(compressorPos, SingularityCompressorBlockEntity.class);
        var data = new CompoundTag();
        MachineTooltips.compressorData(null, compressor, data);
        helper.assertTrue(data.isEmpty(), "No tooltip data for an idle compressor: " + data);
        compressor.getInput().setItemDirect(0, AEItems.SINGULARITY.stack(ModServerConfig.singularitiesPerCompression()));
        for (int i = 0; i < 400; i++) {
            compressor.injectAEPower(compressor.getInternalMaxPower(), Actionable.MODULATE);
            compressor.serverTick();
        }
        MachineTooltips.compressorData(null, compressor, data);
        helper.assertValueEqual(data.getIntOr(MachineTooltips.TAG_PROGRESS, -1), 20, "compressor progress");

        // Imprinter with an item: the item and the progress (half of the ticks = 50 %)
        var itemPos = new BlockPos(2, 1, 0);
        helper.setBlock(itemPos, ModBlocks.IMPRINTER.get());
        var imprinter = helper.getBlockEntity(itemPos, ImprinterBlockEntity.class);
        data = new CompoundTag();
        MachineTooltips.imprinterData(null, imprinter, data);
        helper.assertTrue(data.isEmpty(), "No tooltip data for an idle imprinter: " + data);
        imprinter.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_CELL.get()));
        imprinter.getItemSlot().setItemDirect(0, new ItemStack(Items.COBBLESTONE));
        tickImprinter(imprinter, ModServerConfig.ticksPerImprint() / 2);
        MachineTooltips.imprinterData(null, imprinter, data);
        helper.assertValueEqual(data.getStringOr(MachineTooltips.TAG_TARGET_ITEM, ""), "minecraft:cobblestone", "imprint target");
        helper.assertValueEqual(data.getIntOr(MachineTooltips.TAG_PROGRESS, -1), 50, "imprinter progress");

        // Imprinter with a fluid
        var fluidPos = new BlockPos(4, 1, 0);
        helper.setBlock(fluidPos, ModBlocks.IMPRINTER.get());
        var fluidImprinter = helper.getBlockEntity(fluidPos, ImprinterBlockEntity.class);
        fluidImprinter.getCellSlot().setItemDirect(0, new ItemStack(ModItems.BLANK_INFINITY_FLUID_CELL.get()));
        fluidImprinter.getTank().insert(0, AEFluidKey.of(Fluids.WATER), ImprintTank.CAPACITY, Actionable.MODULATE);
        tickImprinter(fluidImprinter, 10);
        data = new CompoundTag();
        MachineTooltips.imprinterData(null, fluidImprinter, data);
        helper.assertValueEqual(data.getStringOr(MachineTooltips.TAG_TARGET_FLUID, ""), "minecraft:water", "fluid imprint target");
        helper.assertFalse(data.contains(MachineTooltips.TAG_TARGET_ITEM), "No item target for a fluid imprint");

        helper.succeed();
    }

    private static void tickImprinter(ImprinterBlockEntity be, int ticks) {
        for (int i = 0; i < ticks; i++) {
            be.injectAEPower(be.getInternalMaxPower(), Actionable.MODULATE);
            be.serverTick();
        }
    }

    /** Ticks an imprinter with unlimited buffered power until it produces a cell; returns the ticks taken. */
    private static int ticksToImprint(ImprinterBlockEntity be) {
        for (int tick = 1; tick <= 1_000; tick++) {
            be.injectAEPower(be.getInternalMaxPower(), Actionable.MODULATE);
            be.serverTick();
            if (!be.getOutputSlot().getStackInSlot(0).isEmpty()) {
                return tick;
            }
        }
        return -1;
    }

    static void compressorPower(GameTestHelper helper) {
        var pos = new BlockPos(0, 1, 0);
        helper.setBlock(pos, ModBlocks.SINGULARITY_COMPRESSOR.get());
        var be = helper.getBlockEntity(pos, SingularityCompressorBlockEntity.class);
        var bufferSize = ModServerConfig.compressorBaseDraw() * SingularityCompressorBlockEntity.BUFFER_TICKS;
        helper.assertValueEqual(be.getInternalMaxPower(), bufferSize, "buffer size");

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
        helper.assertValueEqual(be.getInternalCurrentPower(), bufferSize, "AE after overfilling");

        helper.succeed();
    }

    static void compressorProcessing(GameTestHelper helper) {
        // Without cards: 10,000,000 AE at 5,000 AE/t = 2,000 ticks
        helper.assertValueEqual(ticksToCompress(helper, new BlockPos(0, 1, 0), 0), 2_000, "ticks without cards");
        // With four cards: 25,000 AE/t = 400 ticks, still 10,000,000 AE in total
        helper.assertValueEqual(ticksToCompress(helper, new BlockPos(2, 1, 0), 4), 400, "ticks with four cards");

        // Eight singularities are not enough: nothing is consumed
        var pos = new BlockPos(4, 1, 0);
        helper.setBlock(pos, ModBlocks.SINGULARITY_COMPRESSOR.get());
        var be = helper.getBlockEntity(pos, SingularityCompressorBlockEntity.class);
        be.getInput().setItemDirect(0, AEItems.SINGULARITY.stack(8));
        for (int i = 0; i < 10; i++) {
            be.injectAEPower(be.getInternalMaxPower(), Actionable.MODULATE);
            be.serverTick();
        }
        helper.assertValueEqual(be.getConsumedEnergy(), 0.0, "energy consumed with 8 singularities");
        helper.assertTrue(be.getOutput().getStackInSlot(0).isEmpty(), "No output with 8 singularities");

        // Taking a singularity out mid-way resets the progress
        be.getInput().insertItem(0, AEItems.SINGULARITY.stack(1), false);
        for (int i = 0; i < 10; i++) {
            be.injectAEPower(be.getInternalMaxPower(), Actionable.MODULATE);
            be.serverTick();
        }
        helper.assertTrue(be.getConsumedEnergy() > 0, "Compressor should be working with 9 singularities");
        be.getInput().extractItem(0, 1, false);
        helper.assertValueEqual(be.getConsumedEnergy(), 0.0, "energy consumed after taking a singularity out");

        helper.succeed();
    }

    static void compressorAutomation(GameTestHelper helper) {
        var pos = new BlockPos(0, 1, 0);
        helper.setBlock(pos, ModBlocks.SINGULARITY_COMPRESSOR.get());
        var be = helper.getBlockEntity(pos, SingularityCompressorBlockEntity.class);
        var items = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(pos), Direction.NORTH);
        helper.assertTrue(items != null, "Compressor should expose an item handler");

        var singularity = ItemResource.of(AEItems.SINGULARITY);
        var compressed = ItemResource.of(ModItems.COMPRESSED_SINGULARITY.get());
        be.getOutput().setItemDirect(0, new ItemStack(ModItems.COMPRESSED_SINGULARITY.get(), 3));

        try (var tx = Transaction.openRoot()) {
            helper.assertValueEqual(items.insert(singularity, 16, tx), 16, "singularities inserted");
            helper.assertValueEqual(items.insert(ItemResource.of(Items.COBBLESTONE), 16, tx), 0, "cobblestone inserted");
            helper.assertValueEqual(items.extract(singularity, 16, tx), 0, "singularities extracted");
            helper.assertValueEqual(items.extract(compressed, 3, tx), 3, "compressed singularities extracted");
            tx.commit();
        }
        helper.assertValueEqual(be.getInput().getStackInSlot(0).getCount(), 16, "singularities in the input");
        helper.assertTrue(be.getOutput().getStackInSlot(0).isEmpty(), "Output should be empty");

        helper.succeed();
    }

    static void fluidCell(GameTestHelper helper) {
        var water = AEFluidKey.of(Fluids.WATER);
        var lava = AEFluidKey.of(Fluids.LAVA);
        var source = IActionSource.empty();

        // Storage: unlimited water, voids inserted water, rejects other fluids
        var inv = StorageCells.getCellInventory(InfinityFluidCellItem.imprinted(water), null);
        helper.assertTrue(inv != null, "Imprinted fluid cell should have an inventory");
        helper.assertValueEqual(inv.extract(water, 1_000_000_000L, Actionable.MODULATE, source), 1_000_000_000L, "extracted water");
        helper.assertValueEqual(inv.insert(water, 1000, Actionable.MODULATE, source), 1000L, "inserted water");
        helper.assertValueEqual(inv.insert(lava, 1000, Actionable.MODULATE, source), 0L, "inserted lava");
        helper.assertFalse(StorageCells.isCellHandled(new ItemStack(ModItems.INFINITY_FLUID_CELL.get())), "Fluid cell without imprint should not be handled");

        helper.succeed();
    }

    /** Runs a compressor with 9 singularities and unlimited buffered power, returning the ticks until it produces. */
    private static int ticksToCompress(GameTestHelper helper, BlockPos pos, int cards) {
        helper.setBlock(pos, ModBlocks.SINGULARITY_COMPRESSOR.get());
        var be = helper.getBlockEntity(pos, SingularityCompressorBlockEntity.class);
        if (cards > 0) {
            be.getUpgrades().addItems(AEItems.SPEED_CARD.stack(cards));
        }
        be.getInput().setItemDirect(0, AEItems.SINGULARITY.stack(9));
        for (int tick = 1; tick <= 10_000; tick++) {
            be.injectAEPower(be.getInternalMaxPower(), Actionable.MODULATE);
            be.serverTick();
            if (tick == 1) {
                helper.assertTrue(be.getBlockState().getValue(FrameMachineBlock.WORKING), "Should show as working");
            }
            if (!be.getOutput().getStackInSlot(0).isEmpty()) {
                helper.assertValueEqual(be.getOutput().getStackInSlot(0).getCount(), 1, "compressed singularities");
                helper.assertTrue(be.getInput().getStackInSlot(0).isEmpty(), "All 9 singularities should be used");
                be.serverTick();
                helper.assertFalse(be.getBlockState().getValue(FrameMachineBlock.WORKING), "Should stop working without input");
                return tick;
            }
        }
        throw helper.assertionException("Compressor never produced output");
    }
}
