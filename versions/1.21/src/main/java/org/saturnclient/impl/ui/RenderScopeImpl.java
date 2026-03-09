package org.saturnclient.impl.ui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import org.saturnclient.common.provider.Providers;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.asset.SpriteRef;
import org.saturnclient.common.ref.game.ItemStackRef;
import org.saturnclient.common.ref.render.MatrixStackRef;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;
import org.saturnclient.ui.resources.SvgTexture;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class RenderScopeImpl implements RenderScope {
    public MatrixStack matrices;
    public VertexConsumerProvider.Immediate vertexConsumers;
    private ScissorStack scissorStack = new ScissorStack();
    private int opacity = 255 << 24;

    public RenderScopeImpl(DrawContext context) {
        this(context.getMatrices(), ((DrawContextAccessor) context).getVertexConsumers());
    }

    public RenderScopeImpl(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
        this.scissorStack = new ScissorStack();
    }

    @Override
    public MatrixStackRef getMatrixStack() {
        return (MatrixStackRef) matrices;
    }

    @Override
    public void setOpacity(float alpha) {
        this.opacity = (int) (alpha * 255) << 24;
    }

    @Override
    public int getColor(int color) {
        int originalAlpha = (color >>> 24) & 0xFF;
        int newAlpha = (opacity >>> 24) & 0xFF;
        int mixedAlpha = (originalAlpha * newAlpha) / 255;
        return (mixedAlpha << 24) | (color & 0x00FFFFFF);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        if (color == 0)
            return;
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

    @Override
    public void drawText(String text, int x, int y, int font, int color) {
        drawText(1.0f, text, x, y, font, color);
    }

    @Override
    public void drawText(float scale, String text, int x, int y, int font, int color) {
        if (color == 0)
            return;
        if (font == 0) {
            scale *= 2;
        }
        int i = 0;
        for (String line : text.split("\n")) {
            color = getColor(color);
            matrices.push();
            matrices.translate(x, y + (i * Fonts.getHeight()), 0);
            matrices.scale(scale, scale, 1.0f);
            TextRenderer textRenderer = SaturnClient.client.textRenderer;
            textRenderer.draw((Text) Fonts.setFont(line, font), 0,
                    font == 0 ? 1 : 7, color, false,
                    this.matrices.peek().getPositionMatrix(),
                    this.vertexConsumers, TextLayerType.NORMAL, 0, 15728880);
            matrices.pop();
            i++;
        }
    }

    private void drawRoundedCorner(int width, int height, int radius, int color) {
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

    @Override
    public void drawRoundedRectangle(int x, int y, int width, int height, int radius, int color) {
        if (color == 0)
            return;
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

    @Override
    public int getScaledWindowWidth() {
        return SaturnClient.client.getWindow().getScaledWidth();
    }

    @Override
    public int getScaledWindowHeight() {
        return SaturnClient.client.getWindow().getScaledHeight();
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height, int color) {
        this.drawTexture(sprite, x, y, u, v, width, height, width, height, width, height, color);
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height) {
        this.drawTexture(sprite, x, y, u, v, width, height, width, height, width, height);
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height,
            int regionWith,
            int regionHeight, int textureWidth, int textureHeight) {
        this.drawTexture(sprite, x, y, u, v, width, height, regionWith, regionHeight, textureWidth, textureHeight, -1);
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height,
            int regionWidth,
            int regionHeight, int textureWidth, int textureHeight, int color) {
        this.drawTexturedQuad((Identifier) (Object) sprite, x, x + width, y, y + height,
                (u + 0.0F) / (float) textureWidth,
                (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight,
                (v + (float) regionHeight) / (float) textureHeight, color);
    }

    /* Minecraft sprites */

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height, int color) {
        this.drawTexture(sprite, x, y, u, v, width, height, width, height, width, height, color);
    }

    public void drawTexture(Identifier sprite, int x, int y, float u, float v, int width, int height) {
        this.drawTexture(sprite, x, y, u, v, width, height, width, height, width, height);
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

        if (sprite.toString().endsWith(".svg")) {
            sprite = (Identifier) (Object) SvgTexture.getSvg(Providers.saturn.getClient(),
                    (IdentifierRef) (Object) sprite,
                    (x2 - x1) * 2, (y2 - y1) * 2);
        }

        if (color == 0)
            return;
        x1 *= 4;
        x2 *= 4;
        y1 *= 4;
        y2 *= 4;
        color = getColor(color);

        matrices.push();
        matrices.scale(0.25f, 0.25f, 1.0f);

        RenderSystem.setShaderTexture(0, sprite);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y1, 0f).texture(u1, v1);
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y2, 0f).texture(u1, v2);
        bufferBuilder.vertex(matrix4f, (float) x2, (float) y2, 0f).texture(u2, v2);
        bufferBuilder.vertex(matrix4f, (float) x2, (float) y1, 0f).texture(u2, v1);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        matrices.pop();
    }

    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
        this.setScissor(this.scissorStack.push(new ScreenRect(x1, y1, x2 - x1, y2 - y1)));
    }

    public void disableScissor() {
        this.setScissor(this.scissorStack.pop());
    }

    public boolean scissorContains(int x, int y) {
        return this.scissorStack.contains(x, y);
    }

    private void setScissor(@Nullable ScreenRect rect) {
        if (rect != null) {
            Window window = SaturnClient.client.getWindow();
            int i = window.getFramebufferHeight();
            double d = window.getScaleFactor();
            double e = (double) rect.getLeft() * d;
            double f = (double) i - (double) rect.getBottom() * d;
            double g = (double) rect.width() * d;
            double h = (double) rect.height() * d;
            RenderSystem.enableScissor((int) e, (int) f, Math.max(0, (int) g), Math.max(0, (int) h));
        } else {
            RenderSystem.disableScissor();
        }

    }

    @Override
    public void draw() {
        this.vertexConsumers.draw();
    }

    public void draw(Consumer<VertexConsumerProvider> drawer) {
        drawer.accept(this.vertexConsumers);
        this.vertexConsumers.draw();
    }

    static class ScissorStack {
        private final Deque<ScreenRect> stack = new ArrayDeque<>();

        ScissorStack() {
        }

        public ScreenRect push(ScreenRect rect) {
            ScreenRect screenRect = (ScreenRect) this.stack.peekLast();
            if (screenRect != null) {
                ScreenRect screenRect2 = (ScreenRect) Objects.requireNonNullElse(rect.intersection(screenRect),
                        ScreenRect.empty());
                this.stack.addLast(screenRect2);
                return screenRect2;
            } else {
                this.stack.addLast(rect);
                return rect;
            }
        }

        @Nullable
        public ScreenRect pop() {
            if (this.stack.isEmpty()) {
                throw new IllegalStateException("Scissor stack underflow");
            } else {
                this.stack.removeLast();
                return (ScreenRect) this.stack.peekLast();
            }
        }

        public boolean contains(int x, int y) {
            return this.stack.isEmpty() ? true : ((ScreenRect) this.stack.peek()).contains(x, y);
        }
    }

    @Override
    public void drawItem(ItemStackRef item, int x, int y) {
        this.drawItem(SaturnClient.client.player, SaturnClient.client.world, item, x, y, 0);
    }

    @Override
    public void drawItem(ItemStackRef stack, int x, int y, int seed) {
        this.drawItem(SaturnClient.client.player, SaturnClient.client.world, stack, x, y, seed);
    }

    @Override
    public void drawItem(ItemStackRef stack, int x, int y, int seed, int z) {
        this.drawItem(SaturnClient.client.player, SaturnClient.client.world, (ItemStack) (Object) stack, x, y, seed, z);
    }

    @Override
    public void drawItemWithoutEntity(ItemStackRef stack, int x, int y) {
        this.drawItemWithoutEntity(stack, x, y, 0);
    }

    @Override
    public void drawItemWithoutEntity(ItemStackRef stack, int x, int y, int seed) {
        this.drawItem((LivingEntity) null, SaturnClient.client.world, stack, x, y, seed);
    }

    private void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStackRef stack, int x, int y,
            int seed) {
        this.drawItem(entity, world, (ItemStack) (Object) stack, x, y, seed, 0);
    }

    private void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed,
            int z) {
        if (!stack.isEmpty()) {
            BakedModel bakedModel = SaturnClient.client.getItemRenderer().getModel(stack, world, entity, seed);
            this.matrices.push();
            this.matrices.translate((float) (x + 8), (float) (y + 8), (float) (150 + (bakedModel.hasDepth() ? z : 0)));

            try {
                this.matrices.scale(16.0F, -16.0F, 16.0F);
                boolean bl = !bakedModel.isSideLit();
                if (bl) {
                    DiffuseLighting.disableGuiDepthLighting();
                }

                SaturnClient.client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false,
                        this.matrices,
                        this.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
                this.draw();
                if (bl) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
                throw new CrashException(crashReport);
            }

            this.matrices.pop();
        }
    }

    @Override
    public void drawSpriteStretched(SpriteRef sprite, int x, int y, int width, int height) {
        this.drawSpriteStretched(sprite, x, y, width, height, -1);
    }

    @Override
    public void drawSpriteStretched(SpriteRef saturnSprite, int x, int y, int width, int height, int color) {
        Sprite sprite = (Sprite) saturnSprite;
        if (color == 0)
            return;
        if (width != 0 && height != 0) {
            this.drawTexturedQuad(sprite.getAtlasId(), x, x + width, y, y + height, sprite.getMinU(), sprite.getMaxU(),
                    sprite.getMinV(), sprite.getMaxV(), color);
        }
    }

    public void fill(int x1, int y1, int x2, int y2, int color) {
        this.fill(x1, y1, x2, y2, 0, color);
    }

    public void fill(int x1, int y1, int x2, int y2, int z, int color) {
        this.fill(RenderLayer.getGui(), x1, y1, x2, y2, z, color);
    }

    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color) {
        this.fill(layer, x1, y1, x2, y2, 0, color);
    }

    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int z, int color) {
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
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

        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(layer);
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, (float) z).color(color);
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, (float) z).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, (float) z).color(color);
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, (float) z).color(color);
    }

    public void drawBorder(int x, int y, int width, int height, int color) {
        this.fill(x, y, x + width, y + 1, color);
        this.fill(x, y + height - 1, x + width, y + height, color);
        this.fill(x, y + 1, x + 1, y + height - 1, color);
        this.fill(x + width - 1, y + 1, x + width, y + height - 1, color);
    }
}
