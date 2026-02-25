package org.saturnclient.saturnclient.cosmetics;

import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
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

    public CloakFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
            EquipmentModelLoader equipmentModelLoader) {
        super(context);
        this.equipmentModelLoader = equipmentModelLoader;
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

    private void renderCape(
            MatrixStack matrices,
            VertexConsumer consumer,
            PlayerEntityRenderState state,
            int light,
            int overlay) {

        float capeWidth = 10.0f / 16.0f;
        float capeLength = 16.0f / 16.0f;
        float thickness = 1.0f / 16.0f;

        float segmentLength = capeLength / PARTS;

        matrices.push();

        // 🔥 Shoulder anchor (this is the important fix)
        matrices.translate(0.0f, 1.25f, 0.125f);

        float accumulatedY = 0.0f;
        float accumulatedZ = 0.0f;

        for (int i = 0; i < PARTS; i++) {

            // 0 → 1 per segment
            float progress = (float) ((PARTS - 1) - i) / PARTS;

            float angle = progress * ((float) Math.PI / 2f);

            double offsetY = -Math.cos(angle) * segmentLength;
            double offsetZ = Math.sin(angle) * segmentLength;

            float topY = accumulatedY;
            float topZ = accumulatedZ;

            accumulatedY += offsetY;
            accumulatedZ += offsetZ;

            float bottomY = accumulatedY;
            float bottomZ = accumulatedZ;

            float x1 = -capeWidth / 2f;
            float x2 = capeWidth / 2f;

            renderCapeQuad(
                    consumer,
                    matrices,
                    new Vec3d(x1, bottomY, bottomZ + thickness),
                    new Vec3d(x2, bottomY, bottomZ + thickness),
                    new Vec3d(x2, topY, topZ + thickness),
                    new Vec3d(x1, topY, topZ + thickness),
                    0f, 0f, 1f, 1f,
                    light,
                    overlay,
                    0f, 0f, 1f,
                    false);
        }

        matrices.pop();
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider providers,
            int light,
            PlayerEntityRenderState state,
            float f,
            float g) {
        if (state.invisible || !state.capeVisible || state.skinTextures.capeTexture() != null) {
            return;
        }

        SaturnPlayer player = SaturnPlayer.get(state.name);
        if (player == null)
            return;

        Identifier cape = Cloaks.getCurrentCloakTexture(player.uuid);
        if (cape == null)
            return;

        VertexConsumer consumer = providers.getBuffer(RenderLayer.getEntityTranslucent(cape));

        renderCape(
                matrices,
                consumer,
                state,
                light,
                OverlayTexture.DEFAULT_UV);
    }
}
