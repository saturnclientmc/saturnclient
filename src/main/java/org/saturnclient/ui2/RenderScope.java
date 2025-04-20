package org.saturnclient.ui2;

import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderScope {
    public MatrixStack matrices;
    public VertexConsumerProvider.Immediate vertexConsumers;

    public RenderScope(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();

        int x1 = x;
        int x2 = x + width;
        int y1 = y;
        int y2 = y + height;

        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, 0).color(color);
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, 0).color(color);
    }

    public void drawTexture(int x, int y, int width, int height, int color) {
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();

        int x1 = x;
        int x2 = x + width;
        int y1 = y;
        int y2 = y + height;

        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, 0).color(color);
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, 0).color(color);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth,
            int textureHeight, int color) {
        this.drawTexture(sprite, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth,
            int textureHeight) {
        this.drawTexture(sprite, x, y, u, v, width, height, width, height, textureWidth, textureHeight);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWith,
            int regionHeight, int textureWidth, int textureHeight) {
        this.drawTexture(sprite, x, y, u, v, width, height, regionWith, regionHeight, textureWidth, textureHeight, -1);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth,
            int regionHeight, int textureWidth, int textureHeight, int color) {
        this.drawTexturedQuad(sprite, x, x + width, y, y + height, (u + 0.0F) / (float) textureWidth,
                (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight,
                (v + (float) regionHeight) / (float) textureHeight, color);
    }

    private void drawTexturedQuad(Identifier sprite, int x1, int x2, int y1, int y2, float u1, float u2, float v1,
            float v2, int color) {
        RenderLayer renderLayer = RenderLayer.getGuiTextured(sprite);
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(renderLayer);
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, 0.0F).texture(u1, v1).color(color);
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, 0.0F).texture(u1, v2).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, 0.0F).texture(u2, v2).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, 0.0F).texture(u2, v1).color(color);
    }

    public void drawText(String text, int x, int y, int color) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.draw(text, (float) x, (float) y, color, false, this.matrices.peek().getPositionMatrix(),
                this.vertexConsumers, TextLayerType.NORMAL, 0, 15728880);
    }
}
