package com.mcupdater.safespawn.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TileRendererInertBeam implements BlockEntityRenderer<TileInertBeacon> {
    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    public TileRendererInertBeam(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileInertBeacon tileInertBeacon, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        long gameTime = tileInertBeacon.getLevel().getGameTime();
        List<TileInertBeacon.BeamSegment> list = tileInertBeacon.getBeamSections();
        int heightOffset = 0;

        for(int segIndex = 0; segIndex < list.size(); ++segIndex) {
            TileInertBeacon.BeamSegment segment = list.get(segIndex);
            renderBeaconBeam(poseStack, buffer, BEAM_LOCATION, partialTicks, 1.0F, gameTime, heightOffset, segIndex == list.size() - 1 ? 1024 : segment.getHeight(), segment.getColor(), 0.2F, 0.25F);
            heightOffset += segment.getHeight();
        }
    }

    public static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation resourceLocation, float partialTicks, float p_228842_4_, long gameTime, int y1, int height, float[] color, float p_228842_10_, float x2) {
        int y2 = y1 + height;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);
        float f = (float)Math.floorMod(gameTime, 40L) + partialTicks;
        float f1 = height < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
        float red = color[0];
        float green = color[1];
        float blue = color[2];
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f * 2.25F - 45.0F));
        float x1 = 0.0F;
        float z2 = 0.0F;
        float x3 = -p_228842_10_;
        float f12 = -p_228842_10_;
        float v2 = -1.0F + f2;
        float v1 = (float)height * p_228842_4_ * (0.5F / p_228842_10_) + v2;
        renderPart(poseStack, buffer.getBuffer(RenderType.beaconBeam(resourceLocation, false)), red, green,  blue, 1.0F, y1, y2, 0.0F, p_228842_10_, p_228842_10_, 0.0F, x3, 0.0F, 0.0F, f12, 0.0F, 1.0F, v1, v2);
        poseStack.popPose();
        x1 = -x2;
        float z1 = -x2;
        z2 = -x2;
        x3 = -x2;
        v2 = -1.0F + f2;
        v1 = (float)height * p_228842_4_ + v2;
        renderPart(poseStack, buffer.getBuffer(RenderType.beaconBeam(resourceLocation, true)), red, green,  blue, 0.125F, y1, y2, x1, z1, x2, z2, x3, x2, x2, x2, 0.0F, 1.0F, v1, v2);
        poseStack.popPose();
    }

    private static void renderPart(PoseStack poseStack, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int y1, int y2, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y1, y2, x1, z1, x2, z2, u1, u2, v1, v2);
        renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y1, y2, x4, z4, x3, z3, u1, u2, v1, v2);
        renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y1, y2, x2, z2, x4, z4, u1, u2, v1, v2);
        renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y1, y2, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderQuad(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int y1, int y2, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y2, x1, z1, u2, v1);
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y1, x1, z1, u2, v2);
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y1, x2, z2, u1, v2);
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, y2, x2, z2, u1, v1);
    }

    private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int y, float x, float z, float u, float v) {
        vertexConsumer.vertex(matrix4f, x, (float)y, z).color(red, green, blue, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240,240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
