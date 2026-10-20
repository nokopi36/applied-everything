package com.nokopi.appliedeverything.client;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlock;
import com.nokopi.appliedeverything.machine.SingularityCompressorBlockEntity;

import appeng.core.definitions.AEItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

/** Floats a spinning singularity in the middle of the compressor while it is compressing. */
public class SingularityCompressorRenderer
        implements BlockEntityRenderer<SingularityCompressorBlockEntity, SingularityCompressorRenderState> {
    // Fits the 6 px gap between the pyramid tips
    private static final float SCALE = 0.4f;
    private static final float DEGREES_PER_SECOND = 90f;

    private final ItemModelResolver itemModelResolver;

    public SingularityCompressorRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public SingularityCompressorRenderState createRenderState() {
        return new SingularityCompressorRenderState();
    }

    @Override
    public void extractRenderState(SingularityCompressorBlockEntity be, SingularityCompressorRenderState state,
            float partialTick, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPos, crumblingOverlay);
        state.item.clear();
        if (!be.getBlockState().getValue(SingularityCompressorBlock.WORKING)) {
            return;
        }
        state.spinDegrees = (System.currentTimeMillis() % 360_000L) / 1000f * DEGREES_PER_SECOND % 360f;
        itemModelResolver.updateForTopItem(state.item, AEItems.SINGULARITY.stack(), ItemDisplayContext.FIXED,
                be.getLevel(), null, (int) be.getBlockPos().asLong());
    }

    @Override
    public void submit(SingularityCompressorRenderState state, PoseStack poseStack, SubmitNodeCollector nodes,
            CameraRenderState cameraRenderState) {
        if (state.item.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.spinDegrees));
        poseStack.scale(SCALE, SCALE, SCALE);
        // Full brightness: the singularity glows on its own
        state.item.submit(poseStack, nodes, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
