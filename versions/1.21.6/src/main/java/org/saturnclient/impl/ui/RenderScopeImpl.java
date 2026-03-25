package org.saturnclient.impl.ui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;

import org.saturnclient.common.provider.Providers;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.asset.SpriteRef;
import org.saturnclient.common.ref.game.ItemStackRef;
import org.saturnclient.common.ref.render.MatrixStackRef;
import org.saturnclient.impl.ref.Matrix3x2fStackRef;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;
import org.saturnclient.ui.resources.SvgTexture;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.ColoredQuadGuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.client.gui.render.state.TextGuiElementRenderState;
import net.minecraft.client.gui.render.state.TexturedQuadGuiElementRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.texture.GuiAtlasManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;

public class RenderScopeImpl implements RenderScope {
    private final Matrix3x2fStackRef matrices;
    private final GuiAtlasManager guiAtlasManager;
    public final GuiRenderState state;

    private ScissorStack scissorStack;
    private int opacity = 255 << 24;
    private final ItemRenderState itemRenderState;

    public RenderScopeImpl(DrawContext context) {
        this(context.getMatrices(), ((DrawContextAccessor) (Object) context).getState());
    }

    public RenderScopeImpl(Matrix3x2fStack matrices, GuiRenderState state) {
        this.state = state;
        this.matrices = new Matrix3x2fStackRef(matrices);
        this.guiAtlasManager = SaturnClient.client.getGuiAtlasManager();
        this.scissorStack = new ScissorStack();
        this.itemRenderState = new ItemRenderState();
    }

    @Override
    public MatrixStackRef getMatrixStack() {
        return matrices;
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
    public void fill(int x1, int y1, int x2, int y2, int color) {
        this.state.addSimpleElement(
                new ColoredQuadGuiElementRenderState(RenderPipelines.GUI, TextureSetup.empty(),
                        new Matrix3x2f(this.matrices.stack),
                        x1, y1, x2,
                        y2, color, color, this.scissorStack.peekLast()));
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int z, int color) {
        this.fill(x1, y1, x2, y2, color);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, int color) {
        if (color == 0)
            return;
        color = getColor(color);
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

        this.fill(x1, y1, x2, y2, color);
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
            this.state.addText(new TextGuiElementRenderState(SaturnClient.client.textRenderer,
                    ((Text) Fonts.setFont(line, font)).asOrderedText(), new Matrix3x2f(this.matrices.stack), x, y,
                    color, 0, false, this.scissorStack.peekLast()));

            matrices.push();
            matrices.translate(x, y + (i * Fonts.getHeight()), 0);
            matrices.scale(scale, scale, 0);
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

            this.drawRectangle(startX, y, w - startX, 1, color);
        }
    }

    private void drawRoundedSide(int cornerWidth, int cornerHeight, int radius, int color) {
        // Top
        this.matrices.push();

        this.matrices.scale(0.05f, 0.05f, 0);

        this.drawRoundedCorner(cornerWidth, cornerHeight, radius * 10, color);

        this.matrices.pop();

        // Bottom
        this.matrices.push();

        this.matrices.translate(cornerWidth, cornerHeight * 2, 0);

        this.matrices.stack.rotate((float) Math.toRadians(90));

        this.matrices.translate(0, -cornerWidth, 0);

        this.matrices.scale(0.05f, 0.05f, 0);

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

        this.matrices.stack.rotate((float) Math.toRadians(90));

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
        GpuTextureView gpuTextureView = SaturnClient.client.getTextureManager().getTexture(sprite).getGlTextureView();

        if (color == 0)
            return;

        x1 *= 4;
        x2 *= 4;
        y1 *= 4;
        y2 *= 4;
        color = getColor(color);

        if (sprite.toString().endsWith(".svg")) {
            sprite = (Identifier) (Object) SvgTexture.getSvg(Providers.saturn.getClient(),
                    (IdentifierRef) (Object) sprite,
                    (x2 - x1) * 2, (y2 - y1) * 2);
        }

        this.state.addSimpleElement(new TexturedQuadGuiElementRenderState(RenderPipelines.GUI,
                TextureSetup.withoutGlTexture(gpuTextureView), new Matrix3x2f(this.matrices.stack), x1, y1, x2, y2, u1,
                u2,
                v1, v2,
                color, this.scissorStack.peekLast()));
    }

    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
        ScreenRect screenRect = (new ScreenRect(x1, y1, x2 - x1, y2 - y1)).transform(this.matrices.stack);
        this.scissorStack.push(screenRect);
    }

    @Override
    public void disableScissor() {
        this.scissorStack.pop();
    }

    @Override
    public boolean scissorContains(int x, int y) {
        return this.scissorStack.contains(x, y);
    }

    public static class ScissorStack {
        private final Deque<ScreenRect> stack = new ArrayDeque();

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

        @Nullable
        public ScreenRect peekLast() {
            return (ScreenRect) this.stack.peekLast();
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
            ItemRenderState itemRenderState = new ItemRenderState();
            SaturnClient.client.getItemModelManager().clearAndUpdate(itemRenderState, stack, ItemDisplayContext.GUI,
                    world,
                    entity, seed);

            try {
                this.state.addItem(new ItemGuiElementRenderState(stack.getItem().getName().toString(),
                        new Matrix3x2f(this.matrices.stack), itemRenderState, x, y, this.scissorStack.peekLast()));
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
                throw new CrashException(crashReport);
            }
        }
    }

    @Override
    public void draw() {
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

    public void drawBorder(int x, int y, int width, int height, int color) {
        this.fill(x, y, x + width, y + 1, color);
        this.fill(x, y + height - 1, x + width, y + height, color);
        this.fill(x, y + 1, x + 1, y + height - 1, color);
        this.fill(x + width - 1, y + 1, x + width, y + height - 1, color);
    }
}
