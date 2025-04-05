package org.saturnclient.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.Pool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class SaturnUi extends Screen {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Pool pool = new Pool(3);
    private float f = 0.0f;

    public static MutableText text(String text) {
        return Text.literal(text).setStyle(
                Style.EMPTY.withFont(Identifier.of("saturnclient:panton")));
    }

    public static void drawHighResTexture(DrawContext context, Identifier t, int x, int y, int w, int h) {
        MatrixStack matricies = context.getMatrices();
        matricies.push();
        matricies.translate(x, y, 0);
        matricies.scale(.25f, .25f, 1.0f);

        context.drawTexture(RenderLayer::getGuiTextured, t, 0, 0, 0, 0,
                w * 4, h * 4, w * 4, h * 4);

        matricies.pop();
    }

    public static void drawHighResTexture(DrawContext context, Identifier t, int x, int y, int w, int h, int c) {
        MatrixStack matricies = context.getMatrices();
        matricies.push();
        matricies.translate(x, y, 0);
        matricies.scale(.25f, .25f, 1.0f);

        context.drawTexture(RenderLayer::getGuiTextured, t, 0, 0, 0, 0,
                w * 4, h * 4, w * 4, h * 4, c);

        matricies.pop();
    }

    public static void drawHighResGuiTexture(DrawContext context, Identifier t, int x, int y, int w, int h, int c) {
        MatrixStack matricies = context.getMatrices();
        matricies.push();
        matricies.translate(x, y, 0);
        matricies.scale(.25f, .25f, 1.0f);

        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                t,
                0,
                0,
                w * 4,
                h * 4,
                c);

        matricies.pop();
    }

    public static int getAlpha(int color, float alpha) {
        return ((int) (alpha * 255) << 24) |
                (color & 0x00FFFFFF);
    }

    public List<SaturnWidget> widgets = new ArrayList<>();
    private int tick = 0;

    public SaturnUi(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        scheduler.scheduleAtFixedRate(
                this::saturnTick,
                0,
                7,
                TimeUnit.MILLISECONDS);
        super.init();
    }

    public void draw(SaturnWidget widget) {
        synchronized (widgets) {
            widget.init();
            widgets.add(widget);
        }
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta) {
        // Blur
        if (!(f < 1.0F)) {
            PostEffectProcessor postEffectProcessor = this.client.getShaderLoader().loadPostEffect(
                    Identifier.ofVanilla("blur"),
                    DefaultFramebufferSet.MAIN_ONLY);
            if (postEffectProcessor != null) {
                postEffectProcessor.setUniforms("Radius", f);
                postEffectProcessor.render(this.client.getFramebuffer(), this.pool);
            }

        }

        for (SaturnWidget widget : new ArrayList<>(widgets)) {
            if (!widget.visible)
                continue;

            double adjustedMouseX = mouseX - widget.x;
            double adjustedMouseY = mouseY - widget.y;
            boolean isMouseInside = adjustedMouseX >= 0 &&
                    adjustedMouseX <= (widget.width * widget.scale) &&
                    adjustedMouseY >= 0 &&
                    adjustedMouseY <= (widget.height * widget.scale);

            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate(widget.x, widget.y, 0);
            matrices.scale(widget.scale, widget.scale, 1.0f);
            widget.render(
                    context,
                    isMouseInside,
                    (int) (adjustedMouseX / widget.scale),
                    (int) (adjustedMouseY / widget.scale));
            matrices.pop();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (SaturnWidget widget : new ArrayList<>(widgets)) {
                widget.focused = false;
                if (!widget.visible)
                    continue;

                double adjustedMouseX = mouseX - widget.x;
                double adjustedMouseY = mouseY - widget.y;
                boolean isMouseInside = adjustedMouseX >= 0 &&
                        adjustedMouseX <= (widget.width * widget.scale) &&
                        adjustedMouseY >= 0 &&
                        adjustedMouseY <= (widget.height * widget.scale);

                if (isMouseInside) {
                    widget.focused = true;
                    widget.click(
                            (int) (adjustedMouseX / widget.scale),
                            (int) (adjustedMouseY / widget.scale));
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SaturnWidget widget : new ArrayList<>(widgets)) {
            if (!widget.visible && !widget.focused)
                continue;

            widget.keyPressed(keyCode, scanCode, modifiers);

            char typedChar = getCharFromKey(keyCode, modifiers);
            if (typedChar != '\0') {
                widget.charTyped(typedChar);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
        for (SaturnWidget widget : new ArrayList<>(widgets)) {
            double adjustedMouseX = mouseX - widget.x;
            double adjustedMouseY = mouseY - widget.y;
            boolean isMouseInside = adjustedMouseX >= 0 &&
                    adjustedMouseX <= (widget.width * widget.scale) &&
                    adjustedMouseY >= 0 &&
                    adjustedMouseY <= (widget.height * widget.scale);

            if (isMouseInside) {
                widget.mouseScrolled(
                        (int) (adjustedMouseX / widget.scale),
                        (int) (adjustedMouseY / widget.scale),
                        horizontalAmount,
                        verticalAmount);
            }
        }

        return super.mouseScrolled(
                mouseX,
                mouseY,
                horizontalAmount,
                verticalAmount);
    }

    private char getCharFromKey(int keyCode, int modifiers) {
        boolean shift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

        // A-Z
        if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) {
            return (char) (shift
                    ? 'A' + (keyCode - GLFW.GLFW_KEY_A)
                    : 'a' + (keyCode - GLFW.GLFW_KEY_A));
        }

        // 0-9
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
            return shift
                    ? ")!@#$%^&*(".charAt(keyCode - GLFW.GLFW_KEY_0)
                    : (char) ('0' + (keyCode - GLFW.GLFW_KEY_0));
        }

        // Special characters
        return switch (keyCode) {
            case GLFW.GLFW_KEY_SPACE -> ' ';
            case GLFW.GLFW_KEY_PERIOD -> shift ? '>' : '.';
            case GLFW.GLFW_KEY_COMMA -> shift ? '<' : ',';
            case GLFW.GLFW_KEY_MINUS -> shift ? '_' : '-';
            case GLFW.GLFW_KEY_EQUAL -> shift ? '+' : '=';
            case GLFW.GLFW_KEY_SEMICOLON -> shift ? ':' : ';';
            case GLFW.GLFW_KEY_APOSTROPHE -> shift ? '"' : '\'';
            case GLFW.GLFW_KEY_SLASH -> shift ? '?' : '/';
            case GLFW.GLFW_KEY_BACKSLASH -> shift ? '|' : '\\';
            case GLFW.GLFW_KEY_LEFT_BRACKET -> shift ? '{' : '[';
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> shift ? '}' : ']';
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> shift ? '~' : '`';
            default -> '\0';
        };
    }

    public void saturnTick() {
        synchronized (scheduler) {
            if (f < 15.0f) {
                f += 0.25f;
            }
        }
        synchronized (widgets) {
            for (SaturnWidget widget : widgets) {
                if (widget.visible && widget.animations != null) {
                    for (SaturnAnimation animation : widget.animations) {
                        if (animation != null &&
                                tick - animation.lastTick == animation.delay) {
                            if (animation.run(widget, tick)) {
                                animation = null;
                            } else {
                                animation.lastTick = tick;
                            }
                        }
                    }
                }

                widget.tick();
            }
            tick++;
        }
    }
}
