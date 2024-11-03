package com.mcupdater.safespawn.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class InertBeaconRenderer implements BlockEntityRenderer<InertBeaconEntity> {
    public static final ResourceLocation BEAM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");
    public static final int MAX_RENDER_Y = 1024;

    public InertBeaconRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(InertBeaconEntity inertBeaconEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        long gameTime = inertBeaconEntity.getLevel().getGameTime();
        List<InertBeaconEntity.BeaconBeamSection> list = inertBeaconEntity.getBeamSections();
        int heightOffset = 0;

        for(int segIndex = 0; segIndex < list.size(); ++segIndex) {
            InertBeaconEntity.BeaconBeamSection segment = list.get(segIndex);
            renderBeaconBeam(
                    poseStack,
                    bufferSource,
                    BEAM_LOCATION,
                    partialTicks,
                    1.0f,
                    gameTime,
                    heightOffset,
                    segIndex == list.size() - 1 ? 1024 : segment.getHeight(),
                    segment.getColor(),
                    0.2F,
                    0.25F);
            heightOffset += segment.getHeight();
        }
    }

    public static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation beamLocation, float partialTicks, float textureScale, long gameTime, int yOffset, int height, int color, float beamRadius, float glowRadius) {
        int y2 = yOffset + height;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        float f = (float) Math.floorMod(gameTime, 40) + partialTicks;
        float f1 = height < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float) Mth.floor(f1 * 0.1F));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        float x1 = 0.0F;
        float z2 = 0.0F;
        float x3 = -beamRadius;
        float f12 = -beamRadius;
        float v2 = -1.0F + f2;
        float v1 = (float)height * textureScale * (0.5F / beamRadius) + v2;
        renderPart(
                poseStack,
                buffer.getBuffer(RenderType.beaconBeam(beamLocation, false)),
                color,
                yOffset,
                y2,
                0.0F,
                beamRadius,
                beamRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                f12,
                0.0F,
                1.0F,
                v1,
                v2
        );
        poseStack.popPose();
        x1 = -glowRadius;
        float z1 = -glowRadius;
        z2 = -glowRadius;
        x3 = -glowRadius;
        v2 = -1.0F + f2;
        v1 = (float)height * textureScale + v2;
        renderPart(
                poseStack,
                buffer.getBuffer(RenderType.beaconBeam(beamLocation, true)),
                FastColor.ARGB32.color(32, color),
                yOffset,
                y2,
                x1,
                z1,
                glowRadius,
                z2,
                x3,
                glowRadius,
                glowRadius,
                glowRadius,
                0.0F,
                1.0F,
                v1,
                v2);
        poseStack.popPose();
    }

    private static void renderPart(PoseStack poseStack, VertexConsumer vertexConsumer, int color, int minY, int maxY, float v1x, float v1z, float v2x, float v2z, float v3x, float v3z, float v4x, float v4z, float minU, float maxU, float minV, float maxV) {
        PoseStack.Pose pose = poseStack.last();
        renderQuad(pose, vertexConsumer, color, minY, maxY, v1x, v1z, v2x, v2z, minU, maxU, minV, maxV);
        renderQuad(pose, vertexConsumer, color, minY, maxY, v4x, v4z, v3x, v3z, minU, maxU, minV, maxV);
        renderQuad(pose, vertexConsumer, color, minY, maxY, v2x, v2z, v4x, v4z, minU, maxU, minV, maxV);
        renderQuad(pose, vertexConsumer, color, minY, maxY, v3x, v3z, v1x, v1z, minU, maxU, minV, maxV);
    }

    private static void renderQuad(PoseStack.Pose pose, VertexConsumer vertexConsumer, int color, int minY, int maxY, float minX, float minZ, float maxX, float maxZ, float minU, float maxU, float minV, float maxV) {
        addVertex(pose, vertexConsumer, color, maxY, minX, minZ, maxU, minV);
        addVertex(pose, vertexConsumer, color, minY, minX, minZ, maxU, maxV);
        addVertex(pose, vertexConsumer, color, minY, maxX, maxZ, minU, maxV);
        addVertex(pose, vertexConsumer, color, maxY, maxX, maxZ, minU, minV);
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, int color, int y, float x, float z, float u, float v) {
        vertexConsumer.addVertex(pose, x, (float)y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public boolean shouldRenderOffScreen(InertBeaconEntity pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(InertBeaconEntity pBlockEntity, Vec3 pCameraPos) {
        return Vec3.atCenterOf(pBlockEntity.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(pCameraPos.multiply(1.0, 0.0, 1.0), (double) this.getViewDistance());
    }

    @Override
    public AABB getRenderBoundingBox(InertBeaconEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, MAX_RENDER_Y, pos.getZ() + 1.0);
    }
}
