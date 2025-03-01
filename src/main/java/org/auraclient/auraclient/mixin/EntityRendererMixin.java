package org.auraclient.auraclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.LightmapTextureManager;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<S extends EntityRenderState> {
    private static final Identifier BADGE_TEXTURE = Identifier.of("auraclient", "textures/player.png");

    @Shadow
    public EntityRenderDispatcher dispatcher;

    @Overwrite
    public void renderLabelIfPresent(S state, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {

        matrices.push();

        matrices.translate(0.0f, 0.0f, 0.0f);

        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);

        double y = state.nameLabelPos.y;

        renderTextAtOrigin(y, text, matrices, vertexConsumers, light);

        if (state instanceof net.minecraft.client.render.entity.state.PlayerEntityRenderState) {
            float yawAngle = 0.0f;
            Quaternionf yawRotation = new Quaternionf().rotateY((float) Math.toRadians(yawAngle));
            matrices.multiply(yawRotation);

            Quaternionf cameraRotation = dispatcher.camera.getRotation();
            Vector3f leftVector = new Vector3f(-1, 0, 0).rotate(cameraRotation);

            float xOffset = textWidth * 0.018f;

            matrices.translate(leftVector.x * xOffset, y + 0.4f, leftVector.z * xOffset);

            matrices.scale(0.25F, 0.25F, 0.25F);

            renderBadgeIcon(matrices, vertexConsumers, light);
        }

        matrices.pop();
    }

    private void renderTextAtOrigin(double y, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light) {
        // Push the current matrix stack
        matrices.push();

        // Translate to the origin (0, 0, 0) and adjust the vertical position slightly
        matrices.translate(0.0f, y + 0.5f, 0.0f);

        // Apply the camera's rotation to face the text towards the camera
        matrices.multiply(dispatcher.getRotation());

        // Scale the text to a readable size
        matrices.scale(0.025F, -0.025F, 0.025F);

        // Get the position matrix for rendering
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        // Obtain the text renderer
        net.minecraft.client.font.TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // Calculate the horizontal offset to center the text
        float textWidth = (float) (-textRenderer.getWidth(text)) / 2.0F;

        // Determine the background opacity
        int backgroundOpacity = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F)
                * 255.0F) << 24;

        // Render the text with a background
        textRenderer.draw(text, textWidth, 0, -2130706433, false, matrix4f, vertexConsumers,
                net.minecraft.client.font.TextRenderer.TextLayerType.SEE_THROUGH, backgroundOpacity, light);

        // Render the text without a background if not sneaking
        textRenderer.draw(text, textWidth, 0, -1, false, matrix4f, vertexConsumers,
                net.minecraft.client.font.TextRenderer.TextLayerType.NORMAL, 0,
                LightmapTextureManager.applyEmission(light, 2));

        // Pop the matrix stack to restore the previous state
        matrices.pop();
    }

    private void renderBadgeIcon(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float size = .5f;
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = vertexConsumers
                .getBuffer(RenderLayer.getEntityTranslucent(BADGE_TEXTURE));

        // Apply rotation to make the icon face the camera
        Quaternionf rotation = dispatcher.camera.getRotation();
        matrices.multiply(rotation);

        vertexConsumer.vertex(matrix, -size, -size, 0).color(255, 255, 255, 255).texture(0, 0).light(light)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, size, -size, 0).color(255, 255, 255, 255).texture(1, 0).light(light)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, size, size, 0).color(255, 255, 255, 255).texture(1, 1).light(light)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
        vertexConsumer.vertex(matrix, -size, size, 0).color(255, 255, 255, 255).texture(0, 1).light(light)
                .overlay(OverlayTexture.DEFAULT_UV).normal(0, 0, 1);
    }
}