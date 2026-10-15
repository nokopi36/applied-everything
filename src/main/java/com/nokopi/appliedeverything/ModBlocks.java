package com.nokopi.appliedeverything;

import com.nokopi.appliedeverything.machine.SingularityCompressorBlock;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AppliedEverything.MODID);

    public static final DeferredBlock<SingularityCompressorBlock> SINGULARITY_COMPRESSOR = BLOCKS.registerBlock("singularity_compressor",
            SingularityCompressorBlock::new,
            p -> p.mapColor(MapColor.METAL).strength(2.2f, 11f).requiresCorrectToolForDrops().sound(SoundType.METAL));

    private ModBlocks() {
    }
}
