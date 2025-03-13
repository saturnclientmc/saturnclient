package org.saturnclient.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SaturnUi extends Screen {
    public List<SaturnWidget> widgets = new ArrayList<>();

    public SaturnUi(Text title) {
        super(title);
    }

    public void draw(SaturnWidget widget) {
        synchronized (widgets) {
            widgets.add(widget);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        for (SaturnWidget widget : new ArrayList<>(widgets)) {
            if (!widget.visible) {
                continue;
            }

            if (widget.animation != null) {
                widget.animation.apply(widget);
            }

            boolean isMouseInside = widget.x < mouseX && widget.x + widget.width > mouseX
                    && widget.y < mouseY && widget.y + widget.height > mouseY;
            widget.render(context, isMouseInside, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (SaturnWidget widget : new ArrayList<>(widgets)) {
                if (!widget.visible) {
                    continue;
                }

                boolean isMouseInside = widget.x < mouseX && widget.x + widget.width > mouseX
                        && widget.y < mouseY && widget.y + widget.height > mouseY;
                if (isMouseInside) {
                    widget.click();
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SaturnWidget widget : new ArrayList<>(widgets)) {
            if (!widget.visible) {
                continue;
            }

            widget.keyPressed(keyCode, scanCode, modifiers);

            char typedChar = getCharFromKey(keyCode, modifiers);
            if (typedChar != '\0') {
                widget.charTyped(typedChar);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private char getCharFromKey(int keyCode, int modifiers) {
        boolean shift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

        // A-Z
        if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) {
            return (char) (shift ? 'A' + (keyCode - GLFW.GLFW_KEY_A) : 'a' + (keyCode - GLFW.GLFW_KEY_A));
        }

        // 0-9
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
            return shift ? ")!@#$%^&*(".charAt(keyCode - GLFW.GLFW_KEY_0) : (char) ('0' + (keyCode - GLFW.GLFW_KEY_0));
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
}
