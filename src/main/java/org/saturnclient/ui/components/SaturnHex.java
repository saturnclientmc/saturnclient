package org.saturnclient.ui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.SaturnWidget;
import org.saturnclient.ui.Textures;

public class SaturnHex extends SaturnWidget {

    private String text = "#"; // Always starts with '#'
    public Property<Integer> prop;
    public int cursorPosition = 1; // After '#'
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public SaturnHex(Property<Integer> prop, int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.prop = prop;
        this.text = intToHex(prop.value);
        this.height = textRenderer.fontHeight + 4;
        this.scale = 0.8f;
    }

    @Override
    public void click(int mouseX, int mouseY) {
        cursorPosition = Math.min(7, text.length()); // Keep cursor inside hex length
    }

    @Override
    public void charTyped(char chr) {
        if (isHexChar(chr) && text.length() < 7) { // Limit to 6 hex digits + '#'
            text = text.substring(0, cursorPosition) +
                    chr +
                    text.substring(cursorPosition);
            cursorPosition++;

            if (text.length() == 7) { // Only update value when full hex is entered
                prop.value = hexToInt(text);
                ConfigManager.save();
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && cursorPosition > 1) { // Prevent deleting '#'
            text = text.substring(0, cursorPosition - 1) +
                    text.substring(cursorPosition);
            cursorPosition--;
        } else if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPosition > 1) {
            cursorPosition--;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPosition < text.length()) {
            cursorPosition++;
        }

        if (text.length() == 1) { // Reset if empty (keep '#')
            text = "#";
            cursorPosition = 1;
        }

        if (text.length() == 7) { // Update only when valid hex
            prop.value = hexToInt(text);
            ConfigManager.save();
        }
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        if (prop.isReset) {
            this.text = intToHex(prop.value);
            this.cursorPosition = 0;
            prop.isReset = false;
        }
        SaturnUi.drawHighResGuiTexture(
                context,
                Textures.BUTTON_BORDER,
                0,
                0,
                this.width,
                this.height,
                focused
                        ? SaturnClientConfig.color.value
                        : SaturnClientConfig.getWhite(this.alpha));

        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        context.drawText(
                textRenderer,
                SaturnUi.text(text),
                2,
                2,
                textColor,
                false);

        if (focused) {
            int cursorX = 2 +
                    textRenderer.getWidth(
                            SaturnUi.text(text.substring(0, cursorPosition)));
            context.fill(
                    cursorX,
                    2,
                    cursorX + 1,
                    textRenderer.fontHeight + 4,
                    0xFFFFFFFF);
        }
    }

    public void clearText() {
        text = "#";
        cursorPosition = 1;
        prop.value = 0xFFFFFFFF; // Default white
    }

    private boolean isHexChar(char chr) {
        return ((chr >= '0' && chr <= '9') ||
                (chr >= 'A' && chr <= 'F') ||
                (chr >= 'a' && chr <= 'f'));
    }

    public static int hexToInt(String hex) {
        hex = hex.replace("#", "");
        int rgb = Integer.parseInt(hex, 16);
        return (0xFF << 24) | rgb;
    }

    public static String intToHex(int color) {
        int rgb = color & 0xFFFFFF;
        return String.format("#%06X", rgb).toLowerCase();
    }
}
