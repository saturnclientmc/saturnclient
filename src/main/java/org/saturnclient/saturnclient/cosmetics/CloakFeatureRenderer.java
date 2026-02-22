package org.saturnclient.saturnclient.cosmetics;

import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.config.SaturnClientConfig;
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
    private static final int PARTS = 16;
    private float currentVelocity = 0.0f;

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

    private float getCurve(int i, float curveMagnitude, PlayerEntityRenderState playerState) {
        float powCurve = (float) Math.pow((double) (PARTS - i) / PARTS, 2) * curveMagnitude;
        float waveFreq = 0.6f;
        float phase = (playerState.age + playerState.handSwingProgress) * 0.4f;
        float sineAmplitude = 0.02f;
        float sineCurve = (float) Math.sin(i * waveFreq + phase) * sineAmplitude;
        float t = Math.min(1.0f, Math.max(0.0f, (curveMagnitude - 0.3f) / 0.2f));
        t = t * t * (3 - 2 * t);
        return (1.0f - t) * powCurve + t * (powCurve + sineCurve);
    }

    private void renderCape(MatrixStack matrixStack, VertexConsumer vertexConsumer,
            PlayerEntityRenderState playerState, int light, int overlay,
            float rotation, float curveMagnitude) {
        float capeWidth = 10.0f / 16.0f;
        float capeHeight = 16.0f / 16.0f;
        float capeDepth = 1.0f / 16.0f;
        float capePartHeight = capeHeight / PARTS;

        matrixStack.push();
        matrixStack.translate(0.0f, 1.25, 0.125f);
        matrixStack.translate(0, -capeHeight, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
        matrixStack.translate(0, capeHeight, 0);

        float x1 = -capeWidth / 2.0f;
        float x2 = capeWidth / 2.0f;
        float y2 = -capeHeight;
        float z1 = 0.0f;
        float z2 = capeDepth;

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

        for (int i = 0; i < PARTS; i++) {
            float y3 = -(capePartHeight * i);
            float y4 = -(capePartHeight * (i + 1));

            float curveOffsetTop = getCurve(i, curveMagnitude, playerState);
            float curveOffsetBottom = getCurve(i + 1, curveMagnitude, playerState);

            // FRONT FACE (outside)
            this.renderCapeQuad(
                    vertexConsumer, matrixStack,
                    new Vec3d(x2, y4, z2 + curveOffsetBottom),
                    new Vec3d(x1, y4, z2 + curveOffsetBottom),
                    new Vec3d(x1, y3, z2 + curveOffsetTop), new Vec3d(x2, y3, z2 + curveOffsetTop),
                    frontU1, frontV2 - (frontPartV * i),
                    frontU2, frontV2 - (frontPartV * (i + 1)),
                    light, overlay,
                    0.0f, 0.0f, 1.0f, false);

            // BACK FACE (inside)
            this.renderCapeQuad(
                    vertexConsumer, matrixStack,
                    new Vec3d(x1, y4, z1 + curveOffsetBottom),
                    new Vec3d(x2, y4, z1 + curveOffsetBottom),
                    new Vec3d(x2, y3, z1 + curveOffsetTop), new Vec3d(x1, y3, z1 + curveOffsetTop),
                    backU1, backV2 - (backPartV * i),
                    backU2, backV2 - (backPartV * (i + 1)),
                    light, overlay,
                    0.0f, 0.0f, 1.0f, false);

            // LEFT FACE (thickness at x2)
            this.renderCapeQuad(
                    vertexConsumer, matrixStack,
                    new Vec3d(x2, y4, z1 + curveOffsetBottom),
                    new Vec3d(x2, y4, z2 + curveOffsetBottom),
                    new Vec3d(x2, y3, z2 + curveOffsetTop), new Vec3d(x2, y3, z1 + curveOffsetTop),
                    leftU1, leftV2 - (leftPartV * i),
                    leftU2, leftV2 - (leftPartV * (i + 1)),
                    light, overlay,
                    1.0f, 0.0f, 0.0f, false);

            // RIGHT FACE (thickness at x1)
            this.renderCapeQuad(
                    vertexConsumer, matrixStack,
                    new Vec3d(x1, y4, z2 + curveOffsetBottom),
                    new Vec3d(x1, y4, z1 + curveOffsetBottom),
                    new Vec3d(x1, y3, z1 + curveOffsetTop), new Vec3d(x1, y3, z2 + curveOffsetTop),
                    rightU1, rightV2 - (rightPartV * i),
                    rightU2, rightV2 - (rightPartV * (i + 1)),
                    light, overlay,
                    -1.0f, 0.0f, 0.0f, false);
        }

        // TOP FACE (shoulders edge) – use its dedicated strip
        matrixStack.push();
        matrixStack.translate(0.0f, y2 + capeDepth, 0.0f);
        matrixStack.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        matrixStack.translate(0.0f, -y2, 0.0f);
        this.renderCapeQuad(
                vertexConsumer, matrixStack,
                new Vec3d(x2, y2 + capeDepth, z2), new Vec3d(x1, y2 + capeDepth, z2),
                new Vec3d(x1, y2, z2), new Vec3d(x2, y2, z2),
                topU1, topV1, topU2, topV2,
                light, overlay,
                0.0f, 1.0f, 0.0f, true);
        matrixStack.pop();

        // BOTTOM FACE – use bottom strip
        float curveOffset = getCurve(0, curveMagnitude, playerState);
        this.renderCapeQuad(
                vertexConsumer, matrixStack,
                new Vec3d(x2, 0.0f, z2 + curveOffset), new Vec3d(x1, 0.0f, z2 + curveOffset),
                new Vec3d(x1, 0.0f, z1 + curveOffset), new Vec3d(x2, 0.0f, z1 + curveOffset),
                bottomU1, bottomV1, bottomU2, bottomV2,
                light, overlay,
                0.0f, 0.0f, 1.0f, false);

        matrixStack.pop();
    }

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

        matrixStack.push();
        VertexConsumer vertexConsumer = vertexConsumerProvider
                .getBuffer(RenderLayer.getEntityAlpha(customCape));

        float targetVelocity = ((6.0F + playerEntityRenderState.field_53537 / 2.0F
                + playerEntityRenderState.field_53536) * 0.02f);
        targetVelocity = Math.max(0.0f, targetVelocity - 0.1f);

        float accelerationRate = 0.02f;
        this.currentVelocity = this.currentVelocity
                + (targetVelocity - this.currentVelocity) * accelerationRate;

        float rotation = this.currentVelocity * 72.0f;
        float curve = this.currentVelocity * 0.4f;

        if (playerEntityRenderState.sneaking && playerEntityRenderState.isInSneakingPose) {
            matrixStack.translate(0, 0.24f, 0);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(27.0f));
            curve += 0.2f;
        }

        matrixStack.translate(0.0f, -0.25f, 0.0f);
        if (this.hasCustomModelForLayer(playerEntityRenderState.equippedChestStack,
                LayerType.HUMANOID)) {
            matrixStack.translate(0.0F, -0.053125F, 0.06875F);
        }

        if (playerEntityRenderState.isSwimming) {
            curve = 0.0f;
            rotation = 0.0f;
        }

        if (!SaturnClientConfig.bendyCloaks.value) {
            curve = 0.0f;
        }

        renderCape(matrixStack, vertexConsumer, playerEntityRenderState, light,
                OverlayTexture.DEFAULT_UV, Math.min(rotation, 60.0f), Math.min(curve, 0.5f));

        matrixStack.pop();
    }
}
