package org.saturnclient.ui2;

import org.joml.Matrix4f;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RenderScope {
    public MatrixStack matrices;
    public VertexConsumerProvider.Immediate vertexConsumers;
    private int opacity = 255 << 24;

    public RenderScope(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
    }

    public void setOpacity(float alpha) {
        this.opacity = (int) (alpha * 255) << 24;
    }

    public int getColor(int color) {
        return opacity | (color & 0x00FFFFFF);
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        color = getColor(color);
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

    public void drawText(String text, int x, int y, boolean bold, int color) {
        color = getColor(color);

        Identifier font = bold ? Fonts.PANTON_BOLD : Fonts.PANTON;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.draw(Fonts.setFont(text, font), (float) x, (float) y, color, false, this.matrices.peek().getPositionMatrix(),
                this.vertexConsumers, TextLayerType.NORMAL, 0, 15728880);
    }

    public void drawRoundedCorner(int width, int height, int radius, int color) {
        int w = width * 10;
        int h = height * 10;
        
        for (int y = 0; y < h; y++) {
            int startX;

            if (y < radius) {
                double dy = y - radius;
                double dx = radius - Math.sqrt(radius * radius - dy * dy);
                startX = (int) Math.ceil(dx);
            } else {
                startX = 0;
            }

            this.drawRect(startX, y, w - startX, 1, color);
        }
    }

    public void drawRoundedRectangle(int x, int y, int width, int height, int radius, int color) {
        int cornerWidth = width / 2;
        int cornerHeight = height / 2;

        this.matrices.push();

        this.matrices.translate(x, y, 0);

        this.matrices.push();

        this.matrices.scale(0.1f, 0.1f, 1.0f);

        this.drawRoundedCorner(cornerWidth, cornerHeight, radius, color);

        this.matrices.pop();

        this.matrices.push();

        this.matrices.translate(cornerWidth, cornerHeight * 2, 0);

        this.matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90));

        this.matrices.translate(0, -cornerWidth, 0);

        this.matrices.scale(0.1f, 0.1f, 1.0f);

        this.drawRoundedCorner(cornerHeight, cornerWidth, radius, color);

        this.matrices.pop();


        this.matrices.push();

        this.matrices.translate(cornerWidth * 2, 0, 0);

        this.matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));

        this.matrices.translate(0, -cornerWidth, 0);

        this.matrices.scale(0.1f, 0.1f, 1.0f);

        this.drawRoundedCorner(cornerWidth, cornerHeight, radius, color);

        this.matrices.pop();


        this.matrices.push();

        this.matrices.translate(cornerWidth, 0, 0);

        this.matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(270));

        this.matrices.translate(0, -cornerWidth, 0);

        this.matrices.scale(0.1f, 0.1f, 1.0f);

        this.drawRoundedCorner(cornerHeight, cornerWidth, radius, color);

        this.matrices.pop();

        this.matrices.pop();
    }
}
