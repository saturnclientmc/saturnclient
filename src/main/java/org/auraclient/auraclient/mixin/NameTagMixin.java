package org.auraclient.auraclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.LightmapTextureManager;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class NameTagMixin<S extends EntityRenderState> {
    private static final Identifier BADGE_TEXTURE = Identifier.of("auraclient", "textures/player-badge.png");

    @Shadow
    public EntityRenderDispatcher dispatcher;

    @Overwrite
    public void renderLabelIfPresent(S state, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        if (state instanceof PlayerEntityRenderState) {
            matrices.push();

            matrices.translate(0.0f, 0.0f, 0.0f);

            double y = state.nameLabelPos.y;

            renderTextAtOrigin(y, text, matrices, vertexConsumers, light);

            matrices.pop();
        } else {
            Vec3d vec3d = state.nameLabelPos;
            if (vec3d != null) {
                boolean bl = !state.sneaking;
                int i = "deadmau5".equals(text.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(vec3d.x, vec3d.y + 0.5, vec3d.z);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                float f = (float) (-textRenderer.getWidth(text)) / 2.0F;
                int j = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
                textRenderer.draw(text, f, (float) i, -2130706433, false, matrix4f, vertexConsumers,
                        bl ? TextLayerType.SEE_THROUGH : TextLayerType.NORMAL, j, light);
                if (bl) {
                    textRenderer.draw(text, f, (float) i, -1, false, matrix4f, vertexConsumers, TextLayerType.NORMAL, 0,
                            LightmapTextureManager.applyEmission(light, 2));
                }

                matrices.pop();
            }
        }
    }

    private void renderTextAtOrigin(double y, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Move up slightly
        matrices.translate(0.0f, y + 0.5f, 0.0f);

        // Apply rotation to face the camera
        matrices.multiply(dispatcher.getRotation());

        // Scale text and badge to a readable size
        matrices.scale(0.025F, -0.025F, 0.025F);

        // Offset the text to avoid Z-fighting
        matrices.translate(0.0F, 0.0F, -0.01F);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        net.minecraft.client.font.TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        float textWidth = textRenderer.getWidth(text);
        float badgeSize = textWidth * 0.20f;
        float totalWidth = textWidth + badgeSize + 6;

        // Adjust text position to make space for badge
        float xStart = -totalWidth / 2 + badgeSize;

        // Draw a single background behind both text and badge
        drawBackground(matrices, vertexConsumers, -totalWidth / 2 - 4, -1.5f, totalWidth, 10, 0x55000000);

        // Render Badge
        renderBadgeIcon(matrices, vertexConsumers, xStart - badgeSize + 1, 3.4f, badgeSize);

        // Render Text with adjusted layer
        textRenderer.draw(text, xStart, 0, -1, false, matrix4f, vertexConsumers,
                net.minecraft.client.font.TextRenderer.TextLayerType.SEE_THROUGH, 0,
                LightmapTextureManager.applyEmission(light, 2));

        matrices.pop();
    }

    /**
     * Draws a single background behind both text and badge.
     */
    private void drawBackground(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            float x, float y, float width, float height, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTextBackgroundSeeThrough());

        int a = (color >> 24) & 255; // Alpha
        int r = (color >> 16) & 255; // Red
        int g = (color >> 8) & 255; // Green
        int b = color & 255; // Blue

        vertexConsumer.vertex(matrix, x, y + height, 0).color(r, g, b, a).texture(0, 1).light(0xF000F0)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, x + width, y + height, 0).color(r, g, b, a).texture(1, 1).light(0xF000F0)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, x + width, y, 0).color(r, g, b, a).texture(1, 0).light(0xF000F0)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, x, y, 0).color(r, g, b, a).texture(0, 0).light(0xF000F0)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
    }

    private void renderBadgeIcon(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float x,
            float y, float size) {
        matrices.push();

        // Position badge correctly
        matrices.translate(x, y, 0);
        matrices.scale(size, size, size); // Scale properly

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = vertexConsumers
                .getBuffer(RenderLayer.getEntityTranslucent(BADGE_TEXTURE));

        int fullBright = LightmapTextureManager.MAX_LIGHT_COORDINATE;

        vertexConsumer.vertex(matrix, -0.5f, -0.5f, 0).color(255, 255, 255, 255).texture(0, 0).light(fullBright)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, 0.5f, -0.5f, 0).color(255, 255, 255, 255).texture(1, 0).light(fullBright)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, 0.5f, 0.5f, 0).color(255, 255, 255, 255).texture(1, 1).light(fullBright)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, -0.5f, 0.5f, 0).color(255, 255, 255, 255).texture(0, 1).light(fullBright)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);

        matrices.pop();
    }
}