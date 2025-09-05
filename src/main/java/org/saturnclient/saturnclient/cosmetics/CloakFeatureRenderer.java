package org.saturnclient.saturnclient.cosmetics;

import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.auth.Auth;
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
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerCapeModel;
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

public class CloakFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final EquipmentModelLoader equipmentModelLoader;
    private final BipedEntityModel<PlayerEntityRenderState> model;
    private static final int PARTS = 16;
    private float currentVelocity = 0.0f;

    public CloakFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
            LoadedEntityModels modelLoader,
            EquipmentModelLoader equipmentModelLoader) {
        super(context);
        this.model = new PlayerCapeModel<PlayerEntityRenderState>(
                modelLoader.getModelPart(EntityModelLayers.PLAYER_CAPE));
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
            // Counter-clockwise winding (front face)
            // Vertex 1 (bottom-left)
            vertexConsumer
                    .vertex(entry.getPositionMatrix(), (float) bottomLeft.x, (float) bottomLeft.y,
                            (float) bottomLeft.z)
                    .color(255, 255, 255, 255)
                    .texture(u1, v2)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);

            // Vertex 2 (bottom-right)
            vertexConsumer
                    .vertex(entry.getPositionMatrix(), (float) bottomRight.x, (float) bottomRight.y,
                            (float) bottomRight.z)
                    .color(255, 255, 255, 255)
                    .texture(u2, v2)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);

            // Vertex 3 (top-right)
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topRight.x, (float) topRight.y,
                    (float) topRight.z)
                    .color(255, 255, 255, 255)
                    .texture(u2, v1)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);

            // Vertex 4 (top-left)
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topLeft.x, (float) topLeft.y,
                    (float) topLeft.z)
                    .color(255, 255, 255, 255)
                    .texture(u1, v1)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);
        } else {
            // Clockwise winding (back face) - reverse vertex order
            // Vertex 4 (top-left)
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topLeft.x, (float) topLeft.y,
                    (float) topLeft.z)
                    .color(255, 255, 255, 255)
                    .texture(u1, v1)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);

            // Vertex 3 (top-right)
            vertexConsumer.vertex(entry.getPositionMatrix(), (float) topRight.x, (float) topRight.y,
                    (float) topRight.z)
                    .color(255, 255, 255, 255)
                    .texture(u2, v1)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);

            // Vertex 2 (bottom-right)
            vertexConsumer
                    .vertex(entry.getPositionMatrix(), (float) bottomRight.x, (float) bottomRight.y,
                            (float) bottomRight.z)
                    .color(255, 255, 255, 255)
                    .texture(u2, v2)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);

            // Vertex 1 (bottom-left)
            vertexConsumer
                    .vertex(entry.getPositionMatrix(), (float) bottomLeft.x, (float) bottomLeft.y,
                            (float) bottomLeft.z)
                    .color(255, 255, 255, 255)
                    .texture(u1, v2)
                    .overlay(overlay)
                    .light(light)
                    .normal(normalX, normalY, normalZ);
        }
    }

    private float getCurve(int i, float curveMagnitude, PlayerEntityRenderState playerState) {
        float powCurve = (float) Math.pow((double) (PARTS - i) / PARTS, 2) * curveMagnitude;

        float waveFreq = 0.6f;
        float phase = (playerState.age + playerState.handSwingProgress) * 0.4f;

        // Sine wave independent of curveMagnitude
        float sineAmplitude = 0.02f; // fixed wiggle strength
        float sineCurve = (float) Math.sin(i * waveFreq + phase) * sineAmplitude;

        // Blend between them
        float t = Math.min(1.0f, Math.max(0.0f, (curveMagnitude - 0.3f) / 0.2f));
        t = t * t * (3 - 2 * t); // smoothstep

        return (1.0f - t) * powCurve + t * (powCurve + sineCurve);
    }

    private void renderCape(MatrixStack matrixStack, VertexConsumer vertexConsumer,
            PlayerEntityRenderState playerState, int light, int overlay, float rotation,
            float curveMagnitude) {
        float capeWidth = 10.0f / 16.0f; // 10 pixels wide
        float capeHeight = 16.0f / 16.0f; // 16 pixels tall
        float capeDepth = 1.0f / 16.0f; // 1 pixel deep
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

        float frontU1 = 1.0f / 64.0f; // Start at pixel 1
        float frontU2 = 11.0f / 64.0f; // End at pixel 11 (10 pixels wide)
        float frontV1 = 1.0f / 32.0f; // Start at pixel 1
        float frontV2 = 17.0f / 32.0f; // End at pixel 17 (16 pixels tall)

        // Back face UV (inside of cape) - typically offset in cape texture
        float backU1 = 12.0f / 64.0f; // Back texture starts after front
        float backU2 = 22.0f / 64.0f; // 10 pixels wide
        float backV2 = 17.0f / 32.0f;

        // Top face UV (edge along shoulders)
        float topU1 = 1.0f / 64.0f; // Start at pixel 1
        float topU2 = 11.0f / 64.0f; // End at pixel 11
        float topV1 = 0.0f / 32.0f; // Start at pixel 0
        float topV2 = 1.0f / 32.0f; // End at pixel 1

        // Bottom face UV (lower edge of cape)
        float bottomU1 = 11.0f / 64.0f; // Start at pixel 11
        float bottomU2 = 22.0f / 64.0f; // End at pixel 22
        float bottomV1 = 0.0f / 32.0f; // Start at pixel 0
        float bottomV2 = 1.0f / 32.0f; // End at pixel 1

        for (int i = 0; i < PARTS; i++) {
            float y3 = -(capePartHeight * i);
            float y4 = -(capePartHeight * (i + 1));
            float totalVHeight = frontV2 - frontV1;
            float partVHeight = totalVHeight / PARTS;

            float curveOffsetTop = getCurve(i, curveMagnitude, playerState);
            float curveOffsetBottom = getCurve(i + 1, curveMagnitude, playerState);

            // FRONT FACE
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    new Vec3d(x2, y4, z2 + curveOffsetBottom), new Vec3d(x1, y4, z2 +
                            curveOffsetBottom),
                    new Vec3d(x1, y3, z2 + curveOffsetTop), new Vec3d(x2, y3, z2 +
                            curveOffsetTop),
                    frontU1, frontV2 - (partVHeight * i), frontU2, frontV2 - (partVHeight * (i +
                            1)),
                    light, overlay,
                    0.0f, 0.0f, 1.0f, false);

            // BACK FACE
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    new Vec3d(x1, y4, z1 + curveOffsetBottom),
                    new Vec3d(x2, y4, z1 + curveOffsetBottom),
                    new Vec3d(x2, y3, z1 + curveOffsetTop), new Vec3d(x1, y3, z1 + curveOffsetTop),
                    backU1, backV2 - (partVHeight * i), backU2, backV2 - (partVHeight * (i + 1)),
                    light, overlay,
                    0.0f, 0.0f, 1.0f, false);

            // LEFT FACE
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    new Vec3d(x2, y4, z1 + curveOffsetBottom),
                    new Vec3d(x2, y4, z2 + curveOffsetBottom),
                    new Vec3d(x2, y3, z2 + curveOffsetTop), new Vec3d(x2, y3, z1 + curveOffsetTop),
                    0.0f, frontV2 - (partVHeight * i), 1.0f / 64.0f,
                    frontV2 - (partVHeight * (i + 1)),
                    light, overlay,
                    1.0f, 0.0f, 0.0f, false);

            // RIGHT FACE
            this.renderCapeQuad(vertexConsumer, matrixStack,
                    new Vec3d(x1, y4, z2 + curveOffsetBottom),
                    new Vec3d(x1, y4, z1 + curveOffsetBottom),
                    new Vec3d(x1, y3, z1 + curveOffsetTop), new Vec3d(x1, y3, z2 + curveOffsetTop),
                    frontU2, frontV2 - (partVHeight * i), frontU2 + 1.0f / 64.0f,
                    frontV2 - (partVHeight * (i + 1)),
                    light, overlay,
                    -1.0f, 0.0f, 0.0f, false);
        }

        // TOP FACE
        matrixStack.push();
        matrixStack.translate(0.0f, y2 + capeDepth, 0.0f);
        matrixStack.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        matrixStack.translate(0.0f, -y2, 0.0f);
        this.renderCapeQuad(vertexConsumer, matrixStack,
                new Vec3d(x2, y2 + capeDepth, z2), new Vec3d(x1, y2 + capeDepth, z2),
                new Vec3d(x1, y2, z2),
                new Vec3d(x2, y2, z2),
                topU1, topV1, topU2, topV2,
                light, overlay,
                0.0f, 1.0f, 0.0f, true);
        matrixStack.pop();

        // BOTTOM FACE
        float curveOffset = getCurve(0, curveMagnitude, playerState);

        this.renderCapeQuad(vertexConsumer, matrixStack,
                new Vec3d(x2, 0.0f, z2 + curveOffset),
                new Vec3d(x1, 0.0f, z2 + curveOffset),
                new Vec3d(x1, 0.0f, z1 + curveOffset), new Vec3d(x2, 0.0f, z1 + curveOffset),
                bottomU1, bottomV1, bottomU2, bottomV2,
                light, overlay,
                0.0f, 0.0f, 1.0f, false);

        matrixStack.pop();
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
            PlayerEntityRenderState playerEntityRenderState, float f, float g) {
        if (playerEntityRenderState.invisible || !playerEntityRenderState.capeVisible) {
            return;
        }

        if (playerEntityRenderState.skinTextures.capeTexture() != null) {
            return;
        }
        String name = playerEntityRenderState.name;
        String uuid = Auth.playerNames.get(name);
        if (uuid == null || !Auth.players.containsKey(uuid)) {
            return;
        }

        Identifier customCape = Cloaks.getCurrentCloakTexture(uuid);
        if (customCape == null
                || this.hasCustomModelForLayer(
                        playerEntityRenderState.equippedChestStack,
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

        if (!playerEntityRenderState.isSwimming && SaturnClientConfig.bendyCloaks.value) {
            float targetVelocity = ((6.0F
                    + playerEntityRenderState.field_53537 / 2.0F
                    + playerEntityRenderState.field_53536) * 0.02f);
            targetVelocity = Math.max(0.0f, targetVelocity - 0.1f);

            float accelerationRate = 0.02f;
            this.currentVelocity = this.currentVelocity
                    + (targetVelocity - this.currentVelocity)
                            * accelerationRate;

            float rotation = this.currentVelocity * 72.0f;
            float curve = this.currentVelocity * 0.4f;

            matrixStack.translate(0.0f, -0.25f, 0.0f);
            if (this.hasCustomModelForLayer(
                    playerEntityRenderState.equippedChestStack,
                    LayerType.HUMANOID)) {
                matrixStack.translate(0.0F, -0.053125F, 0.06875F);
            }

            renderCape(matrixStack, vertexConsumer, playerEntityRenderState, light,
                    OverlayTexture.DEFAULT_UV,
                    Math.min(rotation, 60.0f), Math.min(curve, 0.5f));
        } else {
            if (this.hasCustomModelForLayer(playerEntityRenderState.equippedChestStack,
                    LayerType.HUMANOID)) {
                matrixStack.translate(0.0F, -0.053125F, 0.06875F);
            }
            ((PlayerEntityModel) this.getContextModel()).copyTransforms(this.model);
            this.model.setAngles(playerEntityRenderState);
            this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();
    }
}