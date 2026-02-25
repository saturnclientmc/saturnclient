package org.saturnclient.saturnclient.cosmetics;

import java.util.Arrays;

import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
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
import net.minecraft.util.Identifier;
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
    private static final int PARTS = 16;
    private final float[] segmentValues = new float[PARTS];

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

    private void renderCapeQuad(VertexConsumer vertexConsumer, MatrixStack matrixStack,
            Vec3d bottomLeft, Vec3d bottomRight, Vec3d topRight, Vec3d topLeft,
            float u1, float v1, float u2, float v2, int light, int overlay,
            float normalX, float normalY, float normalZ, boolean flipWinding) {
        MatrixStack.Entry entry = matrixStack.peek();

        if (!flipWinding) {
            // CCW
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) bottomLeft.x, (float) bottomLeft.y,
                    (float) bottomLeft.z)
                    .color(255, 255, 255, 255).texture(u1, v2).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) bottomRight.x, (float) bottomRight.y,
                    (float) bottomRight.z)
                    .color(255, 255, 255, 255).texture(u2, v2).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topRight.x, (float) topRight.y,
                    (float) topRight.z)
                    .color(255, 255, 255, 255).texture(u2, v1).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topLeft.x, (float) topLeft.y,
                    (float) topLeft.z)
                    .color(255, 255, 255, 255).texture(u1, v1).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
        } else {
            // CW
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topLeft.x, (float) topLeft.y,
                    (float) topLeft.z)
                    .color(255, 255, 255, 255).texture(u1, v1).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topRight.x, (float) topRight.y,
                    (float) topRight.z)
                    .color(255, 255, 255, 255).texture(u2, v1).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) bottomRight.x, (float) bottomRight.y,
                    (float) bottomRight.z)
                    .color(255, 255, 255, 255).texture(u2, v2).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) bottomLeft.x, (float) bottomLeft.y,
                    (float) bottomLeft.z)
                    .color(255, 255, 255, 255).texture(u1, v2).overlay(overlay).light(light)
                    .normal(normalX, normalY, normalZ);
        }
    }

    private void renderCape(MatrixStack matrixStack, VertexConsumer vertexConsumer, PlayerEntityRenderState playerState,
            int light, int overlay) {
        float capeWidth = 10.0f / 16.0f;
        float capeHeight = 16.0f / 16.0f;
        float capeDepth = 1.0f / 16.0f;
        float capePartHeight = capeHeight / PARTS;

        float x1 = -capeWidth / 2.0f;
        float x2 = capeWidth / 2.0f;

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

        float curY = 0.0f;
        float curZ = 0.0f;

        for (int i = 0; i < PARTS; i++) {
            float angle = (2.0f - segmentValues[i]) * ((float) Math.PI / 2f);
            float dirY = -(float) Math.cos(angle);
            float dirZ = (float) Math.sin(angle);

            // 1. Thickness at the START of this segment (Must match PREVIOUS segment's end)
            float prevAngle = (i > 0) ? (2.0f - segmentValues[i - 1]) * ((float) Math.PI / 2f) : angle;
            float startAvgAngle = (angle + prevAngle) / 2.0f;
            float thickYStart = (float) Math.sin(startAvgAngle) * capeDepth;
            float thickZStart = (float) Math.cos(startAvgAngle) * capeDepth;

            // 2. Thickness at the END of this segment (Must match NEXT segment's start)
            float nextAngle = (i < PARTS - 1) ? (2.0f - segmentValues[i + 1]) * ((float) Math.PI / 2f) : angle;
            float endAvgAngle = (angle + nextAngle) / 2.0f;
            float thickYEnd = (float) Math.sin(endAvgAngle) * capeDepth;
            float thickZEnd = (float) Math.cos(endAvgAngle) * capeDepth;

            float nextY = curY + dirY * capePartHeight;
            float nextZ = curZ + dirZ * capePartHeight;

            // Inner Vertices (The spine of the cape)
            Vec3d innerTopLeft = new Vec3d(x2, curY, curZ);
            Vec3d innerTopRight = new Vec3d(x1, curY, curZ);
            Vec3d innerBotLeft = new Vec3d(x2, nextY, nextZ);
            Vec3d innerBotRight = new Vec3d(x1, nextY, nextZ);

            // Outer Vertices (Offset by the specific joint thickness)
            Vec3d outerTopLeft = new Vec3d(x2, curY + thickYStart, curZ + thickZStart);
            Vec3d outerTopRight = new Vec3d(x1, curY + thickYStart, curZ + thickZStart);
            Vec3d outerBotLeft = new Vec3d(x2, nextY + thickYEnd, nextZ + thickZEnd);
            Vec3d outerBotRight = new Vec3d(x1, nextY + thickYEnd, nextZ + thickZEnd);

            // FRONT (Inner) - Changed winding/order to fix flipping
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    innerBotLeft, innerBotRight, innerTopRight, innerTopLeft,
                    frontU1, frontV1 + (frontPartV * i), frontU2, frontV1 + (frontPartV * (i + 1)),
                    light, overlay, 0, 0, -1, false);

            // BACK (Outer)
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    outerBotRight, outerBotLeft, outerTopLeft, outerTopRight,
                    backU1, backV1 + (backPartV * i), backU2, backV1 + (backPartV * (i + 1)),
                    light, overlay, 0, 0, 1, false);

            // LEFT EDGE
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    innerBotLeft, outerBotLeft, outerTopLeft, innerTopLeft,
                    leftU1, leftV1 + (leftPartV * i), leftU2, leftV1 + (leftPartV * (i + 1)),
                    light, overlay, 1, 0, 0, false);

            // RIGHT EDGE
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    outerBotRight, innerBotRight, innerTopRight, outerTopRight,
                    rightU1, rightV1 + (rightPartV * i), rightU2, rightV1 + (rightPartV * (i + 1)),
                    light, overlay, -1, 0, 0, false);

            // TOP FACE (Only on first segment)
            if (i == 0) {
                this.renderCapeQuad(vertexConsumer, matrixStack,
                        outerTopLeft, outerTopRight, innerTopRight, innerTopLeft,
                        topU1, topV1, topU2, topV2, light, overlay, 0, 1, 0, false);
            }

            // BOTTOM FACE (Only on last segment)
            if (i == PARTS - 1) {
                this.renderCapeQuad(vertexConsumer, matrixStack,
                        innerBotLeft, innerBotRight, outerBotRight, outerBotLeft,
                        bottomU1, bottomV1, bottomU2, bottomV2, light, overlay, 0, -1, 0, false);
            }

            curY = nextY;
            curZ = nextZ;
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

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
            PlayerEntityRenderState playerEntityRenderState, float f, float g) {
        if (playerEntityRenderState.invisible || !playerEntityRenderState.capeVisible
                || playerEntityRenderState.skinTextures.capeTexture() != null) {
            return;
        }

        SaturnPlayer player = SaturnPlayer.get(playerEntityRenderState.name);

        if (player == null) {
            return;
        }

        Identifier customCape = Cloaks.getCurrentCloakTexture(player.uuid);
        if (customCape == null || this.hasCustomModelForLayer(playerEntityRenderState.equippedChestStack,
                LayerType.WINGS)) {
            return;
        }

        int minBrightness = 7;
        int blockLight = (light >> 4) & 0xF;
        int skyLight = (light >> 20) & 0xF;
        blockLight = Math.max(blockLight, minBrightness);
        skyLight = Math.max(skyLight, minBrightness);
        light = (skyLight << 20) | (blockLight << 4);

        VertexConsumer vertexConsumer = vertexConsumerProvider
                .getBuffer(RenderLayer.getEntityAlpha(customCape));

        matrixStack.push();

        matrixStack.translate(0.0f, 0.0f, 0.19f);

        if (this.hasCustomModelForLayer(playerEntityRenderState.equippedChestStack, LayerType.HUMANOID)) {
            matrixStack.translate(0.0F, -0.053125F, 0.06875F);
        }

        long now = System.currentTimeMillis();
        if (now - lastUpdate >= 20) {
            float value = playerEntityRenderState.field_53537 / 108.0f;
            updateSegmentValues(value);
            lastUpdate = now;
        }

        renderCape(matrixStack, vertexConsumer, playerEntityRenderState, light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }
}
