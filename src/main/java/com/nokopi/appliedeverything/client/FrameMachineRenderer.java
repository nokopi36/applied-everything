package com.nokopi.appliedeverything.client;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nokopi.appliedeverything.machine.FrameMachineBlock;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

/** Floats a spinning item in the middle of a frame machine while it is working. */
public class FrameMachineRenderer<T extends BlockEntity> implements BlockEntityRenderer<T, FrameMachineRenderState> {
    // Fits the 6 px gap between the pyramid tips
    private static final float SCALE = 0.4f;
    private static final float DEGREES_PER_SECOND = 90f;

    private final ItemModelResolver itemModelResolver;
    private final Function<T, ItemStack> shownItem;

    /** @param shownItem the item to float in the middle while the machine works */
    public FrameMachineRenderer(BlockEntityRendererProvider.Context context, Function<T, ItemStack> shownItem) {
        this.itemModelResolver = context.itemModelResolver();
        this.shownItem = shownItem;
    }

    @Override
    public FrameMachineRenderState createRenderState() {
        return new FrameMachineRenderState();
    }

    @Override
    public void extractRenderState(T be, FrameMachineRenderState state, float partialTick, Vec3 cameraPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPos, crumblingOverlay);
        state.item.clear();
        if (!be.getBlockState().getValue(FrameMachineBlock.WORKING)) {
            return;
        }
        var stack = shownItem.apply(be);
        if (stack.isEmpty()) {
            return;
        }
        state.spinDegrees = (System.currentTimeMillis() % 360_000L) / 1000f * DEGREES_PER_SECOND % 360f;
        itemModelResolver.updateForTopItem(state.item, stack, ItemDisplayContext.FIXED, be.getLevel(), null,
                (int) be.getBlockPos().asLong());
    }

    @Override
    public void submit(FrameMachineRenderState state, PoseStack poseStack, SubmitNodeCollector nodes,
            CameraRenderState cameraRenderState) {
        if (state.item.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.spinDegrees));
        poseStack.scale(SCALE, SCALE, SCALE);
        // Full brightness: the item glows on its own
        state.item.submit(poseStack, nodes, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
