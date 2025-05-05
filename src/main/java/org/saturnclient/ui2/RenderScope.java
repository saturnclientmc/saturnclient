package org.saturnclient.ui2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.saturnclient.ui2.resources.Fonts;
import com.mojang.blaze3d.systems.RenderSystem;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RenderScope {
    public MatrixStack matrices;
    public VertexConsumerProvider.Immediate vertexConsumers;
    private Function<Identifier, RenderLayer> renderLayers;
    private ScissorStack scissorStack = new ScissorStack();
    private int opacity = 255 << 24;

    public RenderScope(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
        this.scissorStack = new ScissorStack();
    }

    public void setOpacity(float alpha) {
        this.opacity = (int) (alpha * 255) << 24;
    }

    public int getColor(int color) {
        int originalAlpha = (color >>> 24) & 0xFF;
        int newAlpha = (opacity >>> 24) & 0xFF;
        int mixedAlpha = (originalAlpha * newAlpha) / 255;
        return (mixedAlpha << 24) | (color & 0x00FFFFFF);
    }    

    public void setRenderLayer(Function<Identifier, RenderLayer> renderLayers) {
        this.renderLayers = renderLayers;
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

        matrices.push();
        matrices.translate(x, y, 0);
        matrices.scale(2.0f, 2.0f, 1.0f);
        TextRenderer textRenderer = SaturnClient.client.textRenderer;
        textRenderer.draw(Fonts.setFont(text, font), 0, 0, color, false, this.matrices.peek().getPositionMatrix(),
                this.vertexConsumers, TextLayerType.NORMAL, 0, 15728880);
        matrices.pop();
    }

    public void drawRoundedCorner(int width, int height, int radius, int color) {
        int w = width * 20;
        int h = height * 20;
    
        for (int y = 0; y < h; y++) {
            int startX = 0;
    
            if (y < radius) {
                double dy = radius - y - 0.5;
                double dx = Math.sqrt(Math.max(0, radius * radius - dy * dy));
                startX = radius - (int) dx;
            }
    
            this.drawRect(startX, y, w - startX, 1, color);
        }
    }    

    private void drawRoundedSide(int cornerWidth, int cornerHeight, int radius, int color) {
        // Top
        this.matrices.push();

        this.matrices.scale(0.05f, 0.05f, 1.0f);

        this.drawRoundedCorner(cornerWidth, cornerHeight, radius * 10, color);

        this.matrices.pop();

        // Bottom
        this.matrices.push();

        this.matrices.translate(cornerWidth, cornerHeight * 2, 0);

        this.matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90));

        this.matrices.translate(0, -cornerWidth, 0);

        this.matrices.scale(0.05f, 0.05f, 1.0f);

        this.drawRoundedCorner(cornerHeight, cornerWidth, radius * 10, color);

        this.matrices.pop();
    }

    public void drawRoundedRectangle(int x, int y, int width, int height, int radius, int color) {
        radius = Math.min(radius, Math.min(width, height));
        int cornerWidth = width / 2;
        int cornerHeight = height / 2;

        this.matrices.push();

        this.matrices.translate(x, y, 0);

        this.drawRoundedSide(cornerWidth, cornerHeight, radius, color);

        this.matrices.translate(cornerWidth * 2, cornerHeight * 2, 0);

        this.matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));

        this.matrices.translate(0, 0, 0);

        this.drawRoundedSide(cornerWidth, cornerHeight, radius, color);

        this.matrices.pop();
    }

    public int getScaledWindowWidth() {
        return SaturnClient.client.getWindow().getScaledWidth();
     }
  
    public int getScaledWindowHeight() {
       return SaturnClient.client.getWindow().getScaledHeight();
    }
    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int color) {
       this.drawTexture(sprite, x, y, u, v, width, height, width, height, width, height, color);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height) {
       this.drawTexture(sprite, x, y, u, v, width, height, width, height, width, height);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWith, int regionHeight, int textureWidth, int textureHeight) {
       this.drawTexture(sprite, x, y, u, v, width, height, regionWith, regionHeight, textureWidth, textureHeight, -1);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color) {
       this.drawTexturedQuad(sprite, x, x + width, y, y + height, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, color);
    }

    private void drawTexturedQuad(Identifier sprite, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, int color) {
        x1 *= 4;
        x2 *= 4;
        y1 *= 4;
        y2 *= 4;
        color = getColor(color);

        matrices.push();
        matrices.scale(0.25f, 0.25f, 1.0f);
       RenderSystem.setShaderTexture(0, sprite);
       RenderLayer renderLayer = (RenderLayer)renderLayers.apply(sprite);
       Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
       VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(renderLayer);
       vertexConsumer.vertex(matrix4f, (float)x1, (float)y1, 0.0F).texture(u1, v1).color(color);
       vertexConsumer.vertex(matrix4f, (float)x1, (float)y2, 0.0F).texture(u1, v2).color(color);
       vertexConsumer.vertex(matrix4f, (float)x2, (float)y2, 0.0F).texture(u2, v2).color(color);
       vertexConsumer.vertex(matrix4f, (float)x2, (float)y1, 0.0F).texture(u2, v1).color(color);
       matrices.pop();
    }

    public void enableScissor(int x1, int y1, int x2, int y2) {
        ScreenRect screenRect = (new ScreenRect(x1, y1, x2 - x1, y2 - y1)).transform(this.matrices.peek().getPositionMatrix());
        this.setScissor(this.scissorStack.push(screenRect));
     }
  
     public void disableScissor() {
        this.setScissor(this.scissorStack.pop());
     }
  
     public boolean scissorContains(int x, int y) {
        return this.scissorStack.containsPoint(x, y);
     }
  
     private void setScissor(@Nullable ScreenRect rect) {
        this.draw();
        if (rect != null) {
           Window window = SaturnClient.client.getWindow();
           int i = window.getFramebufferHeight();
           double d = window.getScaleFactor();
           double e = (double)rect.getLeft() * d;
           double f = (double)i - (double)rect.getBottom() * d;
           double g = (double)rect.width() * d;
           double h = (double)rect.height() * d;
           RenderSystem.enableScissor((int)e, (int)f, Math.max(0, (int)g), Math.max(0, (int)h));
        } else {
           RenderSystem.disableScissor();
        }
  
    }

    public void draw() {
        this.vertexConsumers.draw();
    }

    static class ScissorStack {
        private final Deque<ScreenRect> stack = new ArrayDeque<>();

        public ScreenRect push(ScreenRect p_281812_) {
            ScreenRect screenrectangle = this.stack.peekLast();
            if (screenrectangle != null) {
                ScreenRect screenrectangle1 = Objects.requireNonNullElse(p_281812_.intersection(screenrectangle), ScreenRect.empty());
                this.stack.addLast(screenrectangle1);
                return screenrectangle1;
            } else {
                this.stack.addLast(p_281812_);
                return p_281812_;
            }
        }

        @Nullable
        public ScreenRect pop() {
            if (this.stack.isEmpty()) {
                throw new IllegalStateException("Scissor stack underflow");
            } else {
                this.stack.removeLast();
                return this.stack.peekLast();
            }
        }

        public boolean containsPoint(int p_329411_, int p_333404_) {
            return this.stack.isEmpty() ? true : this.stack.peek().contains(p_329411_, p_333404_);
        }
    }
}
