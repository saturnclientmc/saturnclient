package org.saturnclient.impl.cosmetics;

import java.util.Arrays;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.config.Config;
import org.saturnclient.cosmetics.Cloaks;
import org.saturnclient.impl.cosmetics.utils.ShaderUtils;
import org.saturnclient.saturnclient.SaturnRenderState;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.equipment.EquipmentModel.LayerType;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

/**
 * The cloak renderer
 * - **Efficient**: uses a image only consisting of the cloak texture, 16x8
 * whitespace.
 * - **Curving**: Curves when player velocity increases.
 * - **Wavey**: Makes the cloak feel like wind, when jump sprinting.
 */
public class CloakFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final EquipmentModelLoader equipmentModelLoader;
    private static final int PARTS = 24;
    private final float[] segmentValues = new float[PARTS];
    private static final int H_PARTS = 6;
    private float horizontalCurve = 0.0f;

    private static final float TEX_W = 176f;
    private static final float TEX_H = 138f;

    private static final class PxRect {
        final int x, y, w, h;

        PxRect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        float u1() {
            return x / TEX_W;
        }

        float v1() {
            return y / TEX_H;
        }

        float u2() {
            return (x + w) / TEX_W;
        }

        float v2() {
            return (y + h) / TEX_H;
        }
    }

    private static final PxRect FRONT_RECT = new PxRect(8, 8, 80, 128);
    private static final PxRect BACK_RECT = new PxRect(96, 8, 80, 128);
    private static final PxRect LEFT_EDGE_RECT = new PxRect(0, 8, 8, 128);
    private static final PxRect RIGHT_EDGE_RECT = new PxRect(88, 8, 8, 128);
    private static final PxRect TOP_RECT = new PxRect(8, 0, 80, 8);
    private static final PxRect BOTTOM_RECT = new PxRect(88, 0, 80, 8);

    public CloakFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
            EquipmentModelLoader equipmentModelLoader) {
        super(context);
        this.equipmentModelLoader = equipmentModelLoader;
        Arrays.fill(segmentValues, 0.0f);
    }

    private boolean hasCustomModelForLayer(ItemStack stack, EquipmentModel.LayerType layerType) {
        EquippableComponent equippableComponent = (EquippableComponent) stack
                .get(DataComponentTypes.EQUIPPABLE);
        if (equippableComponent != null && !equippableComponent.assetId().isEmpty()) {
            EquipmentModel equipmentModel = this.equipmentModelLoader
                    .get((RegistryKey<EquipmentAsset>) equippableComponent.assetId().get());
            return !equipmentModel.getLayers(layerType).isEmpty();
        } else {
            return false;
        }
    }

    private void vertex(VertexConsumer vc, Matrix4f posMat, Matrix3f normalMat,
            Vec3d pos, int color,
            float u, float v, int overlay, int light,
            float nx, float ny, float nz) {

        Vector4f p = new Vector4f((float) pos.x, (float) pos.y, (float) pos.z, 1.0f);
        p.mul(posMat);

        Vector3f n = new Vector3f(nx, ny, nz);
        n.mul(normalMat);

        vc.vertex(
                p.x(), p.y(), p.z(),
                color,
                u, v,
                overlay, light,
                n.x(), n.y(), n.z());
    }

    private void renderCapeQuad(VertexConsumer vertexConsumer, MatrixStack.Entry entry,
            Vec3d bottomLeft, Vec3d bottomRight, Vec3d topRight, Vec3d topLeft,
            float u1, float v1, float u2, float v2, int light, int overlay,
            float normalX, float normalY, float normalZ, boolean flipWinding) {
        // Extract matrix (you must transform positions manually now)
        Matrix4f pos = entry.getPositionMatrix();
        Matrix3f normalMat = entry.getNormalMatrix();

        int color = 0xFFFFFFFF; // white RGBA

        if (!flipWinding) {
            // CCW
            vertex(vertexConsumer, pos, normalMat, bottomLeft, color, u1, v2, overlay, light, normalX, normalY,
                    normalZ);
            vertex(vertexConsumer, pos, normalMat, bottomRight, color, u2, v2, overlay, light, normalX, normalY,
                    normalZ);
            vertex(vertexConsumer, pos, normalMat, topRight, color, u2, v1, overlay, light, normalX, normalY, normalZ);
            vertex(vertexConsumer, pos, normalMat, topLeft, color, u1, v1, overlay, light, normalX, normalY, normalZ);
        } else {
            // CW
            vertex(vertexConsumer, pos, normalMat, topLeft, color, u1, v1, overlay, light, normalX, normalY, normalZ);
            vertex(vertexConsumer, pos, normalMat, topRight, color, u2, v1, overlay, light, normalX, normalY, normalZ);
            vertex(vertexConsumer, pos, normalMat, bottomRight, color, u2, v2, overlay, light, normalX, normalY,
                    normalZ);
            vertex(vertexConsumer, pos, normalMat, bottomLeft, color, u1, v2, overlay, light, normalX, normalY,
                    normalZ);
        }
    }

    private void renderCape(MatrixStack.Entry entry, VertexConsumer vertexConsumer,
            PlayerEntityRenderState playerState, int light, int overlay) {

        float capeWidth = 10.0f / 16.0f;
        float capeHeight = 16.0f / 16.0f;
        float capeDepth = 1.0f / 16.0f;

        final float frontU1 = FRONT_RECT.u1();
        final float frontU2 = FRONT_RECT.u2();
        final float frontV1 = FRONT_RECT.v1();
        final float frontV2 = FRONT_RECT.v2();
        final float frontPartV = (frontV2 - frontV1) / PARTS;

        final float backU1 = BACK_RECT.u1();
        final float backU2 = BACK_RECT.u2();
        final float backV1 = BACK_RECT.v1();
        final float backV2 = BACK_RECT.v2();
        final float backPartV = (backV2 - backV1) / PARTS;

        final float leftU1 = LEFT_EDGE_RECT.u1();
        final float leftU2 = LEFT_EDGE_RECT.u2();
        final float leftV1 = LEFT_EDGE_RECT.v1();
        final float leftV2 = LEFT_EDGE_RECT.v2();
        final float leftPartV = (leftV2 - leftV1) / PARTS;

        final float rightU1 = RIGHT_EDGE_RECT.u1();
        final float rightU2 = RIGHT_EDGE_RECT.u2();
        final float rightV1 = RIGHT_EDGE_RECT.v1();
        final float rightV2 = RIGHT_EDGE_RECT.v2();
        final float rightPartV = (rightV2 - rightV1) / PARTS;

        final float topU1 = TOP_RECT.u1();
        final float topU2 = TOP_RECT.u2();
        final float topV1 = TOP_RECT.v1();
        final float topV2 = TOP_RECT.v2();

        final float bottomU1 = BOTTOM_RECT.u1();
        final float bottomU2 = BOTTOM_RECT.u2();
        final float bottomV1 = BOTTOM_RECT.v1();
        final float bottomV2 = BOTTOM_RECT.v2();

        // =============================
        // 1. Spine
        // =============================

        float[] spineY = new float[PARTS + 1];
        float[] spineZ = new float[PARTS + 1];

        float partHeight = capeHeight / PARTS;

        for (int i = 0; i < PARTS; i++) {
            float angle = (2.0f - segmentValues[i]) * ((float) Math.PI / 2f);
            float dirY = -(float) Math.cos(angle);
            float dirZ = (float) Math.sin(angle);

            spineY[i + 1] = spineY[i] + dirY * partHeight;
            spineZ[i + 1] = spineZ[i] + dirZ * partHeight;
        }

        // =============================
        // 2. Grid
        // =============================

        Vec3d[][] inner = new Vec3d[H_PARTS + 1][PARTS + 1];
        Vec3d[][] outer = new Vec3d[H_PARTS + 1][PARTS + 1];

        for (int x = 0; x <= H_PARTS; x++) {

            float t = (float) x / H_PARTS;
            float xPos = -capeWidth / 2.0f + capeWidth * t;

            float centered = t - 0.5f;
            float curve = (centered * centered) * 0.3f;
            float zCurve = (curve * horizontalCurve) / PARTS;

            for (int i = 0; i <= PARTS; i++) {
                float y = spineY[i];
                float z = spineZ[i];

                int idx = Math.min(i, PARTS - 1);
                // angle: 0 is horizontal (y-flat), PI/2 is vertical (z-flat)
                float angle = (2.0f - segmentValues[idx]) * ((float) Math.PI / 2f);

                float cosA = (float) Math.cos(angle);
                float sinA = (float) Math.sin(angle);

                // Calculate how much the horizontal curve shifts the vertex
                float currentCurve = zCurve * i;

                // Rotate the curve offset so it aligns with the cape's tilt
                float curveY = sinA * currentCurve;
                float curveZ = cosA * currentCurve;

                float thickY = sinA * capeDepth;
                float thickZ = cosA * capeDepth;

                inner[x][i] = new Vec3d(xPos, y - curveY, z - curveZ);
                outer[x][i] = new Vec3d(xPos, (y - curveY) - thickY, (z - curveZ) - thickZ);
            }

        }

        // =============================
        // 3. Front + Back
        // =============================

        for (int x = 0; x < H_PARTS; x++) {

            float uStart = frontU1 + (frontU2 - frontU1) * (1.0f - ((float) x / H_PARTS));
            float uEnd = frontU1 + (frontU2 - frontU1) * (1.0f - ((float) (x + 1) / H_PARTS));

            float buStart = backU1 + (backU2 - backU1) * ((float) x / H_PARTS);
            float buEnd = backU1 + (backU2 - backU1) * ((float) (x + 1) / H_PARTS);

            for (int i = 0; i < PARTS; i++) {

                float vStart = frontV1 + frontPartV * i;
                float vEnd = frontV1 + frontPartV * (i + 1);

                float bvStart = backV1 + backPartV * i;
                float bvEnd = backV1 + backPartV * (i + 1);

                Vec3d innerTL = inner[x + 1][i];
                Vec3d innerTR = inner[x][i];
                Vec3d innerBL = inner[x + 1][i + 1];
                Vec3d innerBR = inner[x][i + 1];

                Vec3d outerTL = outer[x + 1][i];
                Vec3d outerTR = outer[x][i];
                Vec3d outerBL = outer[x + 1][i + 1];
                Vec3d outerBR = outer[x][i + 1];

                // FRONT
                renderCapeQuad(vertexConsumer, entry,
                        outerBR, outerBL, outerTL, outerTR,
                        uStart, vStart, uEnd, vEnd,
                        light, overlay, 0, 0, 1, false);

                // BACK
                renderCapeQuad(vertexConsumer, entry,
                        innerBL, innerBR, innerTR, innerTL,
                        buEnd, bvStart, buStart, bvEnd,
                        light, overlay, 0, 0, -1, false);
            }
        }

        // =============================
        // 4. LEFT SIDE (x = H_PARTS)
        // =============================

        for (int i = 0; i < PARTS; i++) {

            Vec3d innerTop = inner[H_PARTS][i];
            Vec3d innerBot = inner[H_PARTS][i + 1];
            Vec3d outerTop = outer[H_PARTS][i];
            Vec3d outerBot = outer[H_PARTS][i + 1];

            float vStart = leftV1 + leftPartV * i;
            float vEnd = leftV1 + leftPartV * (i + 1);

            renderCapeQuad(vertexConsumer, entry,
                    innerTop, outerTop, outerBot, innerBot,
                    leftU1, vEnd,
                    leftU2, vStart,
                    light, overlay, -1, 0, 0, false);
        }

        // =============================
        // 5. RIGHT SIDE (x = 0)
        // =============================

        for (int i = 0; i < PARTS; i++) {

            Vec3d innerTop = inner[0][i];
            Vec3d innerBot = inner[0][i + 1];
            Vec3d outerTop = outer[0][i];
            Vec3d outerBot = outer[0][i + 1];

            float vStart = rightV1 + rightPartV * i;
            float vEnd = rightV1 + rightPartV * (i + 1);

            renderCapeQuad(vertexConsumer, entry,
                    outerTop, innerTop, innerBot, outerBot,
                    rightU1, vEnd,
                    rightU2, vStart,
                    light, overlay, 1, 0, 0, false);
        }

        // =============================
        // 6. TOP (i = 0)
        // =============================

        for (int x = 0; x < H_PARTS; x++) {

            Vec3d outerL = outer[x][0];
            Vec3d outerR = outer[x + 1][0];
            Vec3d innerR = inner[x + 1][0];
            Vec3d innerL = inner[x][0];

            float uStart = topU1 + (topU2 - topU1) * ((float) x / H_PARTS);
            float uEnd = topU1 + (topU2 - topU1) * ((float) (x + 1) / H_PARTS);

            renderCapeQuad(vertexConsumer, entry,
                    outerL, outerR, innerR, innerL,
                    uStart, topV1, uEnd, topV2,
                    light, overlay, 0, 1, 0, false);
        }

        // =============================
        // 7. BOTTOM (i = PARTS)
        // =============================

        for (int x = 0; x < H_PARTS; x++) {

            Vec3d innerL = inner[x][PARTS];
            Vec3d innerR = inner[x + 1][PARTS];
            Vec3d outerR = outer[x + 1][PARTS];
            Vec3d outerL = outer[x][PARTS];

            float uStart = bottomU1 + (bottomU2 - bottomU1) * ((float) x / H_PARTS);
            float uEnd = bottomU1 + (bottomU2 - bottomU1) * ((float) (x + 1) / H_PARTS);

            renderCapeQuad(vertexConsumer, entry,
                    innerL, innerR, outerR, outerL,
                    uStart, bottomV1, uEnd, bottomV2,
                    light, overlay, 0, -1, 0, false);
        }
    }

    // Shift array left and insert new value at the start
    private void updateSegmentValues(float value) {
        // Shift all elements one position to the right (from end to start)
        for (int i = segmentValues.length - 1; i > 0; i--) {
            segmentValues[i] = segmentValues[i - 1];
        }

        // Insert the new value at index 0
        segmentValues[0] = value;
    }

    private long lastUpdate = 0;

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, PlayerEntityRenderState state,
            float limbAngle, float limbDistance) {
        if (state.invisible || !state.capeVisible
                || state.skinTextures.cape() != null) {
            return;
        }

        SaturnPlayer player = state.getData(SaturnRenderState.saturnDataKey);

        if (player == null) {
            return;
        }

        IdentifierRef customCape = Cloaks.getCurrentCloakTexture(player.cloak);
        if (customCape == null
                || this.hasCustomModelForLayer(state.equippedChestStack, LayerType.WINGS)) {
            return;
        }

        int minBrightness = 7;
        int blockLight = (light >> 4) & 0xF;
        int skyLight = (light >> 20) & 0xF;
        blockLight = Math.max(blockLight, minBrightness);
        skyLight = Math.max(skyLight, minBrightness);
        light = (skyLight << 20) | (blockLight << 4);

        matrices.push();

        matrices.translate(0.0f, 0.0f, 0.12f);

        if (this.hasCustomModelForLayer(state.equippedChestStack, LayerType.HUMANOID)) {
            matrices.translate(0.0F, -0.053125F, 0.06875F);
        }

        if (state.isInSneakingPose) {
            matrices.translate(0, 0.16f, 0.0f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(29.0f));
        }

        long now = System.currentTimeMillis();
        if (now - lastUpdate >= 20) {
            float velX = Math.min(0.8f, state.field_53537 / 108.0f);
            float rawVelY = state.field_53536;
            float velY = (rawVelY > 3.4f ? rawVelY : rawVelY < -3.4f ? rawVelY : 0.0f) / 16;

            float value = Math.max(0.0f, Math.min(2.0f, state.isSwimming ? 0.0f : velX + velY));

            if (Config.cloakPhysics.value) {
                updateSegmentValues(segmentValues[0] + (value - segmentValues[0]) * 0.3f);
            } else {
                Arrays.fill(segmentValues, segmentValues[0] + (value - segmentValues[0]) * 0.05f);
            }
            lastUpdate = now;
        }

        final int l = light;

        horizontalCurve = MathHelper.clamp(state.field_53537 / 150.0f, -1.0f, 1.0f);

        queue.submitCustom(matrices, ShaderUtils.getRenderLayer(customCape), (entry, vertexConsumer) -> {
            renderCape(entry, vertexConsumer, state, l, OverlayTexture.DEFAULT_UV);
        });

        matrices.pop();
    }
}
