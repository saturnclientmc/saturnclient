package org.saturnclient.ui2.components.inputs;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public abstract class Input extends Element {
    String text = "";
    public int cursorPosition = 0;

    public abstract void checkReset();

    public abstract void backspace();

    public boolean validateBackspace() {
        return true;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (cursorPosition > 0 && validateBackspace()) {
                text = text.substring(0, cursorPosition - 1) +
                        text.substring(cursorPosition);
                cursorPosition--;
                backspace();
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

    @Override
    public void click(int mouseX, int mouseY) {
        int scrollOffset = getScrollOffset();
        String visibleText = text.substring(scrollOffset);

        int newCursorPos = scrollOffset;
        float textWidth = 0;

        for (int i = 0; i < visibleText.length(); i++) {
            textWidth = SaturnClient.client.textRenderer.getWidth(visibleText.substring(0, i + 1)) * 0.6f;

            if (textWidth > mouseX) {
                break;
            }

            newCursorPos++;
        }

        cursorPosition = newCursorPos;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        checkReset();

        renderScope.drawRoundedRectangle(0, 0, width, height, 10, 0xFF000000);

        int scrollOffset = getScrollOffset();
        String visibleText = getVisibleText(scrollOffset);
        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        renderScope.drawText(0.6f, visibleText, 4, 4, Theme.FONT.value, textColor);

        if (focused) {
            int cursorX = 4 +
                    (int) (Fonts.getWidth(visibleText.substring(0, cursorPosition - scrollOffset), Theme.FONT.value)
                            * 0.6f);

            renderScope.drawRect(cursorX, 2, 1, height - 4, 0xFFFFFFFF);
        }
    }

    protected int getScrollOffset() {
        if (text.isEmpty()) {
            return 0;
        }

        int offset = 0;
        int maxWidth = width - 4; // Available width for text
        while (offset < cursorPosition) { // Ensure cursor remains visible
            String visibleText = text.substring(offset, cursorPosition);
            if (Fonts.getWidth(visibleText, Theme.FONT.value) > maxWidth) {
                offset++;
            } else {
                break;
            }
        }
        return offset;
    }

    protected String getVisibleText(int scrollOffset) {
        String visibleText = text.substring(scrollOffset);

        // Ensure text does not overflow past the available width
        for (int i = 1; i <= visibleText.length(); i++) {
            if (Fonts.getWidth(visibleText.substring(0, i), Theme.FONT.value) > width - 4) {
                return visibleText.substring(0, i - 1); // Trim overflow
            }
        }
        return visibleText;
    }
}
