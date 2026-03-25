package org.saturnclient.impl.ui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

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
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;

public class RenderScopeImpl implements RenderScope {
    // ----------------------------
    // Fields
    // ----------------------------
    public MatrixStack matrices;
    public VertexConsumerProvider.Immediate vertexConsumers;
    private ScissorStack scissorStack = new ScissorStack();
    private int opacity = 255 << 24;
    private final ItemRenderState itemRenderState;

    // ----------------------------
    // Constructors
    // ----------------------------
    public RenderScopeImpl(DrawContext context) {
        this(context.getMatrices(), ((DrawContextAccessor) context).getVertexConsumers());
    }

    public RenderScopeImpl(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
        this.scissorStack = new ScissorStack();
        this.itemRenderState = new ItemRenderState();
    }

    // ----------------------------
    // Matrix & Transform
    // ----------------------------
    @Override
    public MatrixStackRef getMatrixStack() {
        return (MatrixStackRef) matrices;
    }

    // ----------------------------
    // Color & Opacity
    // ----------------------------
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

    // ----------------------------
    // Draw / Window Info
    // ----------------------------
    @Override
    public void draw() {
        this.vertexConsumers.draw();
    }

    @Override
    public int getScaledWindowWidth() {
        return SaturnClient.client.getWindow().getScaledWidth();
    }

    @Override
    public int getScaledWindowHeight() {
        return SaturnClient.client.getWindow().getScaledHeight();
    }

    public void draw(Consumer<VertexConsumerProvider> drawer) {
        drawer.accept(this.vertexConsumers);
        this.vertexConsumers.draw();
    }

    // ----------------------------
    // Shapes
    // ----------------------------
    @Override
    public void drawRectangle(int x, int y, int width, int height, int color) {
        if (color == 0)
            return;
        color = getColor(color);

        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();

        int x1 = x;
        int x2 = x + width;
        int y1 = y;
        int y2 = y + height;

        if (x1 < x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y1 < y2) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, x1, y1, 0).color(color);
        vertexConsumer.vertex(matrix4f, x1, y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, x2, y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, x2, y1, 0).color(color);
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int color) {
        fill(x1, y1, x2, y2, 0, color);
    }

    public void fill(int x1, int y1, int x2, int y2, int z, int color) {
        fill(RenderLayer.getGui(), x1, y1, x2, y2, z, color);
    }

    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color) {
        fill(layer, x1, y1, x2, y2, 0, color);
    }

    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int z, int color) {
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        if (x1 < x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y1 < y2) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(layer);
        vertexConsumer.vertex(matrix4f, x1, y1, z).color(color);
        vertexConsumer.vertex(matrix4f, x1, y2, z).color(color);
        vertexConsumer.vertex(matrix4f, x2, y2, z).color(color);
        vertexConsumer.vertex(matrix4f, x2, y1, z).color(color);
    }

    @Override
    public void drawBorder(int x, int y, int width, int height, int color) {
        fill(x, y, x + width, y + 1, color);
        fill(x, y + height - 1, x + width, y + height, color);
        fill(x, y + 1, x + 1, y + height - 1, color);
        fill(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    // ----------------------------
    // Text
    // ----------------------------
    @Override
    public void drawText(String text, int x, int y, int font, int color) {
        drawText(1.0f, text, x, y, font, color);
    }

    @Override
    public void drawText(float scale, String text, int x, int y, int font, int color) {
        if (color == 0)
            return;
        if (font == 0)
            scale *= 2;

        int i = 0;
        for (String line : text.split("\n")) {
            color = getColor(color);
            matrices.push();
            matrices.translate(x, y + i * Fonts.getHeight(), 0);
            matrices.scale(scale, scale, 1.0f);
            TextRenderer textRenderer = SaturnClient.client.textRenderer;
            textRenderer.draw((Text) Fonts.setFont(line, font), 0, font == 0 ? 1 : 7, color, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextLayerType.NORMAL, 0, 15728880);
            matrices.pop();
            i++;
        }
    }

    // ----------------------------
    // Textures & Sprites
    // ----------------------------
    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height) {
        drawTexture(sprite, x, y, u, v, width, height, width, height, width, height);
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height, int color) {
        drawTexture(sprite, x, y, u, v, width, height, width, height, width, height, color);
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height,
            int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        drawTexture(sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, -1);
    }

    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height,
            int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color) {
        drawTexturedQuad((Identifier) (Object) sprite, x, x + width, y, y + height,
                (u + 0.0f) / textureWidth, (u + regionWidth) / textureWidth,
                (v + 0.0f) / textureHeight, (v + regionHeight) / textureHeight, color);
    }

    @Override
    public void drawSpriteStretched(SpriteRef sprite, int x, int y, int width, int height) {
        drawSpriteStretched(sprite, x, y, width, height, -1);
    }

    @Override
    public void drawSpriteStretched(SpriteRef saturnSprite, int x, int y, int width, int height, int color) {
        Sprite sprite = (Sprite) saturnSprite;
        if (color == 0)
            return;
        if (width != 0 && height != 0) {
            drawTexturedQuad(sprite.getAtlasId(), x, x + width, y, y + height,
                    sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV(), color);
        }
    }

    private void drawTexturedQuad(Identifier sprite, int x1, int x2, int y1, int y2,
            float u1, float u2, float v1, float v2, int color) {
        if (sprite.toString().endsWith(".svg")) {
            sprite = (Identifier) (Object) SvgTexture.getSvg((IdentifierRef) (Object) sprite, (x2 - x1) * 2,
                    (y2 - y1) * 2);
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
        RenderLayer renderLayer = RenderLayer.getGuiTextured(sprite);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        vertexConsumer.vertex(matrix4f, x1, y1, 0).texture(u1, v1).color(color);
        vertexConsumer.vertex(matrix4f, x1, y2, 0).texture(u1, v2).color(color);
        vertexConsumer.vertex(matrix4f, x2, y2, 0).texture(u2, v2).color(color);
        vertexConsumer.vertex(matrix4f, x2, y1, 0).texture(u2, v1).color(color);
        matrices.pop();
    }

    // ----------------------------
    // Items
    // ----------------------------
    @Override
    public void drawItem(ItemStackRef item, int x, int y) {
        drawItem(SaturnClient.client.player, SaturnClient.client.world, item, x, y, 0);
    }

    private void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStackRef stack, int x, int y,
            int seed) {
        drawItem(entity, world, (ItemStack) (Object) stack, x, y, seed, 0);
    }

    private void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed,
            int z) {
        if (stack.isEmpty())
            return;

        SaturnClient.client.getItemModelManager().update(itemRenderState, stack, ItemDisplayContext.GUI, world, entity,
                seed);

        if (!stack.isEmpty()) {
            SaturnClient.client.getItemModelManager().clearAndUpdate(this.itemRenderState, stack,
                    ItemDisplayContext.GUI, world,
                    entity, seed);
            this.matrices.push();
            this.matrices.translate((float) (x + 8), (float) (y + 8), (float) (150 + z));

            try {
                this.matrices.scale(16.0F, -16.0F, 16.0F);
                boolean bl = !this.itemRenderState.isSideLit();
                if (bl) {
                    this.draw();
                    DiffuseLighting.disableGuiDepthLighting();
                }

                this.itemRenderState.render(this.matrices, this.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV);
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

    // ----------------------------
    // Scissor / Clipping
    // ----------------------------
    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
        ScreenRect screenRect = new ScreenRect(x1, y1, x2 - x1, y2 - y1)
                .transform(matrices.peek().getPositionMatrix());
        setScissor(scissorStack.push(screenRect));
    }

    @Override
    public void disableScissor() {
        setScissor(scissorStack.pop());
    }

    @Override
    public boolean scissorContains(int x, int y) {
        return scissorStack.containsPoint(x, y);
    }

    private void setScissor(@Nullable ScreenRect rect) {
        draw();
        if (rect != null) {
            Window window = SaturnClient.client.getWindow();
            int framebufferHeight = window.getFramebufferHeight();
            double scaleFactor = window.getScaleFactor();
            RenderSystem.enableScissor(
                    (int) (rect.getLeft() * scaleFactor),
                    (int) (framebufferHeight - rect.getBottom() * scaleFactor),
                    Math.max(0, (int) (rect.width() * scaleFactor)),
                    Math.max(0, (int) (rect.height() * scaleFactor)));
        } else {
            RenderSystem.disableScissor();
        }
    }

    static class ScissorStack {
        private final Deque<ScreenRect> stack = new ArrayDeque<>();

        public ScreenRect push(ScreenRect p_281812_) {
            ScreenRect screenrectangle = this.stack.peekLast();
            if (screenrectangle != null) {
                ScreenRect screenrectangle1 = Objects.requireNonNullElse(p_281812_.intersection(screenrectangle),
                        ScreenRect.empty());
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
