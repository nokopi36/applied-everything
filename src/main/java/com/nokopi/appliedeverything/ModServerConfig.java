package com.nokopi.appliedeverything;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Machine costs and the Infinity Energy Cell capacity, for modpack makers (config/applied_everything-server.toml,
 * or per world in serverconfig/). Synced from the server to clients.
 * <p>
 * Read through the static getters: before a world is loaded the defaults are used.
 */
public final class ModServerConfig {
    private static final double MAX_ENERGY = 1e15;

    public static final ModServerConfig CONFIG;
    public static final ModConfigSpec SPEC;

    static {
        var pair = new ModConfigSpec.Builder().configure(ModServerConfig::new);
        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }

    private final ModConfigSpec.IntValue singularitiesPerCompression;
    private final ModConfigSpec.DoubleValue energyPerCompression;
    private final ModConfigSpec.DoubleValue compressorBaseDraw;
    private final ModConfigSpec.DoubleValue compressorDrawPerCard;
    private final ModConfigSpec.DoubleValue energyPerImprint;
    private final ModConfigSpec.IntValue ticksPerImprint;
    private final ModConfigSpec.DoubleValue energyCellChargeRate;
    private final ModConfigSpec.IntValue energyCellCapacity;

    private ModServerConfig(ModConfigSpec.Builder builder) {
        builder.push("singularity_compressor");
        singularitiesPerCompression = builder
                .comment("Singularities consumed by one compression")
                .defineInRange("singularities_per_compression", 9, 1, 64);
        energyPerCompression = builder
                .comment("AE consumed by one compression")
                .defineInRange("energy_per_compression", 10_000_000.0, 1.0, MAX_ENERGY);
        compressorBaseDraw = builder
                .comment("AE drawn per tick without Acceleration Cards")
                .defineInRange("base_draw_per_tick", 5_000.0, 1.0, MAX_ENERGY);
        compressorDrawPerCard = builder
                .comment("Additional AE drawn per tick for each Acceleration Card. The total per compression stays the same")
                .defineInRange("draw_per_card", 5_000.0, 0.0, MAX_ENERGY);
        builder.pop();

        builder.push("infinity_imprinter");
        energyPerImprint = builder
                .comment("AE consumed by one imprint")
                .defineInRange("energy_per_imprint", 100_000.0, 1.0, MAX_ENERGY);
        ticksPerImprint = builder
                .comment("Ticks one imprint takes when power keeps up")
                .defineInRange("ticks_per_imprint", 100, 1, 72_000);
        energyCellChargeRate = builder
                .comment("AE per tick pushed into an Infinity Energy Cell being charged")
                .defineInRange("energy_cell_charge_rate", 50_000.0, 1.0, MAX_ENERGY);
        builder.pop();

        builder.push("infinity_energy_cell");
        energyCellCapacity = builder
                .comment("FE needed before an Infinity Energy Cell becomes infinite (requires Applied Flux).",
                        "Raising it in an existing world makes full cells partly charged again until they are recharged")
                .defineInRange("capacity_fe", 1_000_000_000, 1, Integer.MAX_VALUE);
        builder.pop();
    }

    private static <T> T get(ModConfigSpec.ConfigValue<T> value) {
        return SPEC.isLoaded() ? value.get() : value.getDefault();
    }

    public static int singularitiesPerCompression() {
        return get(CONFIG.singularitiesPerCompression);
    }

    public static double energyPerCompression() {
        return get(CONFIG.energyPerCompression);
    }

    public static double compressorBaseDraw() {
        return get(CONFIG.compressorBaseDraw);
    }

    public static double compressorDrawPerCard() {
        return get(CONFIG.compressorDrawPerCard);
    }

    public static double energyPerImprint() {
        return get(CONFIG.energyPerImprint);
    }

    public static int ticksPerImprint() {
        return get(CONFIG.ticksPerImprint);
    }

    public static double energyCellChargeRate() {
        return get(CONFIG.energyCellChargeRate);
    }

    public static int energyCellCapacity() {
        return get(CONFIG.energyCellCapacity);
    }
}
