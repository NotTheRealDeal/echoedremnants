package net.ntrdeal.echoedremnants.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.ntrdeal.echoedremnants.block.ModBlocks;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class SculkShriekerRenderer implements BlockEntityRenderer<SculkShriekerBlockEntity, SculkShriekerRenderer.RenderState> {
    private static final float SCALE = 1.25f;
    private static final float MULTI_SCALE = 1f / SCALE;
    private static final Supplier<ItemStack> SHARD = Suppliers.memoize(Items.ECHO_SHARD::getDefaultInstance);
    private final ItemModelResolver itemResolver;

    public SculkShriekerRenderer(BlockEntityRendererProvider.Context context) {
        this.itemResolver = context.itemModelResolver();
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            SculkShriekerBlockEntity entity, RenderState state, float partialTicks,
            Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        int shardCount = entity.getBlockState().getValue(ModBlocks.SHARDS_PROPERTY);
        state.shards = shardCount;
        if (shardCount > 0) this.itemResolver.appendItemLayers(
                state.stackState, SHARD.get(), ItemDisplayContext.GROUND,
                entity.getLevel(), null, (int) state.blockPos.asLong()
        );
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int shards = Math.clamp(state.shards, 0, 4);
        if (shards == 0) return;

        poseStack.pushPose();
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(0f, scaled(0.55f), scaled(0.5f));

        for (int index = 0; index < shards; index++) {
            poseStack.translate(scaled(0.5f), 0f, scaled(0.5f));
            poseStack.rotate(Axis.YP.rotationDegrees(90f));
            poseStack.pushPose();
            poseStack.translate(scaled(-0.05f), 0f, 0f);
            poseStack.rotate(Axis.ZN.rotationDegrees(45f));
            state.stackState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static float scaled(float input) {
        return input * MULTI_SCALE;
    }

    public static class RenderState extends BlockEntityRenderState {
        ItemStackRenderState stackState = new ItemStackRenderState();
        int shards = 0;
    }
}
