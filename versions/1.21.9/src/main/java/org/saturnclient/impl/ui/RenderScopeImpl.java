package org.saturnclient.impl.ui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.textures.GpuTextureView;

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
import net.minecraft.client.gui.render.state.special.EntityGuiElementRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.KeyedItemRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureSetup;
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

    public final Matrix3x2fStackRef matrices;
    public final GuiRenderState state;

    private ScissorStack scissorStack;
    private int opacity = 0xFF << 24;

    public RenderScopeImpl(DrawContext context) {
        this(context.getMatrices(), ((DrawContextAccessor) (Object) context).getState());
    }

    public RenderScopeImpl(Matrix3x2fStack matrices, GuiRenderState state) {
        this.matrices = new Matrix3x2fStackRef(matrices);
        this.state = state;
        this.scissorStack = new ScissorStack();
    }

    @Override
    public MatrixStackRef getMatrixStack() {
        return matrices;
    }

    @Override
    public void setOpacity(float alpha) {
        this.opacity = ((int) (alpha * 255)) << 24;
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
                new ColoredQuadGuiElementRenderState(
                        RenderPipelines.GUI,
                        TextureSetup.empty(),
                        new Matrix3x2f(this.matrices.stack),
                        x1, y1, x2, y2, color, color,
                        this.scissorStack.peekLast()));
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

        if (x2 < x1) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y2 < y1) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
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
        if (font == 0)
            scale *= 2;

        int lineIndex = 0;
        for (String line : text.split("\n")) {
            int finalColor = getColor(color);

            matrices.push();
            matrices.translate(x, y + (lineIndex * Fonts.getHeight()));
            matrices.scale(scale, scale);

            this.state.addText(
                    new TextGuiElementRenderState(
                            SaturnClient.client.textRenderer,
                            ((Text) Fonts.setFont(line, font)).asOrderedText(),
                            new Matrix3x2f(this.matrices.stack),
                            0, font == 0 ? 1 : 8,
                            finalColor, 0, false, this.scissorStack.peekLast()));

            matrices.pop();

            lineIndex++;
        }
    }

    @Override
    public int getScaledWindowWidth() {
        return SaturnClient.client.getWindow().getScaledWidth();
    }

    @Override
    public int getScaledWindowHeight() {
        return SaturnClient.client.getWindow().getScaledHeight();
    }

    // Texture rendering
    @Override
    public void drawTexture(IdentifierRef sprite, int x, int y, float u, float v, int width, int height) {
        drawTexture(sprite, x, y, u, v, width, height, -1);
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
                (u + 0f) / textureWidth,
                (u + regionWidth) / textureWidth,
                (v + 0f) / textureHeight,
                (v + regionHeight) / textureHeight,
                color);
    }

    private void drawTexturedQuad(Identifier sprite, int x1, int x2, int y1, int y2,
            float u1, float u2, float v1, float v2, int color) {
        if (color == 0)
            return;
        color = getColor(color);

        if (sprite.getPath().endsWith(".svg")) {
            sprite = (Identifier) (Object) SvgTexture.getSvg(
                    (IdentifierRef) (Object) sprite,
                    (x2 - x1) * 2, (y2 - y1) * 2);
        }

        GpuTextureView gpuTextureView = SaturnClient.client.getTextureManager()
                .getTexture(sprite).getGlTextureView();

        this.state.addSimpleElement(new TexturedQuadGuiElementRenderState(
                RenderPipelines.GUI_TEXTURED,
                TextureSetup.withoutGlTexture(gpuTextureView),
                new Matrix3x2f(this.matrices.stack),
                x1, y1, x2, y2, u1, u2, v1, v2, color,
                this.scissorStack.peekLast()));
    }

    // Scissor management
    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
        ScreenRect rect = new ScreenRect(x1, y1, x2 - x1, y2 - y1).transform(this.matrices.stack);
        scissorStack.push(rect);
    }

    @Override
    public void disableScissor() {
        scissorStack.pop();
    }

    @Override
    public boolean scissorContains(int x, int y) {
        return scissorStack.contains(x, y);
    }

    public static class ScissorStack {
        private final Deque<ScreenRect> stack = new ArrayDeque<>();

        public ScreenRect push(ScreenRect rect) {
            ScreenRect last = stack.peekLast();
            if (last != null) {
                ScreenRect intersection = Objects.requireNonNullElse(rect.intersection(last), ScreenRect.empty());
                stack.addLast(intersection);
                return intersection;
            } else {
                stack.addLast(rect);
                return rect;
            }
        }

        @Nullable
        public ScreenRect pop() {
            if (stack.isEmpty())
                throw new IllegalStateException("Scissor stack underflow");
            stack.removeLast();
            return stack.peekLast();
        }

        @Nullable
        public ScreenRect peekLast() {
            return stack.peekLast();
        }

        public boolean contains(int x, int y) {
            return stack.isEmpty() || stack.peekLast().contains(x, y);
        }
    }

    // Item rendering
    @Override
    public void drawItem(ItemStackRef stack, int x, int y) {
        drawItem(null, SaturnClient.client.world, stack, x, y, 0);
    }

    private void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStackRef stack, int x, int y,
            int seed) {
        drawItem(entity, world, (ItemStack) (Object) stack, x, y, seed, 0);
    }

    private void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed,
            int z) {
        if (!stack.isEmpty()) {
            KeyedItemRenderState keyedItemRenderState = new KeyedItemRenderState();
            SaturnClient.client.getItemModelManager().clearAndUpdate(keyedItemRenderState, stack,
                    ItemDisplayContext.GUI, world,
                    entity, seed);

            try {
                this.state.addItem(new ItemGuiElementRenderState(stack.getItem().getName().toString(),
                        new Matrix3x2f(this.matrices.stack), keyedItemRenderState, x, y,
                        this.scissorStack.peekLast()));
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
    public void drawSpriteStretched(SpriteRef sprite, int x, int y, int width, int height) {
        drawSpriteStretched(sprite, x, y, width, height, -1);
    }

    @Override
    public void drawSpriteStretched(SpriteRef sprite, int x, int y, int width, int height, int color) {
        Sprite mcSprite = (Sprite) sprite;
        if (color == 0 || width == 0 || height == 0)
            return;

        drawTexturedQuad(mcSprite.getAtlasId(), x, x + width, y, y + height,
                mcSprite.getMinU(), mcSprite.getMaxU(), mcSprite.getMinV(), mcSprite.getMaxV(), color);
    }

    @Override
    public void draw() {
        // No-op for now
    }

    public void drawBorder(int x, int y, int width, int height, int color) {
        fill(x, y, x + width, y + 1, color);
        fill(x, y + height - 1, x + width, y + height, color);
        fill(x, y + 1, x + 1, y + height - 1, color);
        fill(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    public void addEntity(EntityRenderState entityState, float scale, Vector3f translation, Quaternionf rotation,
            @Nullable Quaternionf overrideCameraAngle, int x1, int y1, int x2, int y2) {

        Matrix3x2f mat = this.matrices.stack;
        float offsetX = mat.m20;
        float offsetY = mat.m21;
        float scaleX = mat.m00; // current matrix X scale
        float scaleY = mat.m11; // current matrix Y scale

        int adjustedX1 = (int) (x1 * scaleX + offsetX);
        int adjustedY1 = (int) (y1 * scaleY + offsetY);
        int adjustedX2 = (int) (x2 * scaleX + offsetX);
        int adjustedY2 = (int) (y2 * scaleY + offsetY);

        float adjustedScale = scale * scaleX; // apply matrix scale to entity scale too

        this.state.addSpecialElement(new EntityGuiElementRenderState(entityState, translation, rotation,
                overrideCameraAngle, adjustedX1, adjustedY1, adjustedX2, adjustedY2, adjustedScale,
                this.scissorStack.peekLast()));
    }
}