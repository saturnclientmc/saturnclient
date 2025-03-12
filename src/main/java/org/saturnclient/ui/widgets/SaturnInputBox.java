package org.saturnclient.ui.widgets;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.ui.SaturnWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnInputBox extends SaturnWidget {
    public static final Identifier TEXTURE = Identifier.ofVanilla("widget/saturn/button");

    public String text = "";
    public String placeholder = "";
    public int cursorPosition = 0;
    public boolean focused = false;
    private TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public SaturnInputBox(String placeholder, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
    }

    @Override
    public void click() {
        focused = true;
    }

    @Override
    public void charTyped(char chr) {
        if (focused) {
            text = text.substring(0, cursorPosition) + chr
                    + text.substring(cursorPosition);

            cursorPosition++;
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (focused) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (cursorPosition > 0) {
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                    cursorPosition--;
                }
            } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
                if (cursorPosition > 0) {
                    cursorPosition--;
                }
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                if (cursorPosition < text.length()) {
                    cursorPosition++;
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, boolean hovering) {
        context.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURE, this.x,
                this.y, this.width, this.height, ColorHelper.getWhite(this.alpha));

        int scrollOffset = getScrollOffset();
        String visibleText = text.substring(scrollOffset);

        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        context.drawText(textRenderer, visibleText, x + 5, y + 5, textColor, false);

        if (focused) {
            int cursorX = x + 5 + textRenderer.getWidth(visibleText.substring(0, cursorPosition - scrollOffset));
            context.fill(cursorX, y + 3, cursorX + 1, y + height - 3, 0xFFFFFFFF);
        }
    }

    public void clearText() {
        text = "";
        cursorPosition = 0;
    }

    private int getScrollOffset() {
        if (text.isEmpty()) {
            return 0;
        }

        int offset = 0;

        if (cursorPosition == text.length()) {
            while (textRenderer.getWidth(text.substring(offset)) > width) {
                offset++;
            }
        } else {
            while (textRenderer.getWidth(text.substring(offset, cursorPosition)) > width) {
                offset++;
            }
        }

        return offset;
    }
}
