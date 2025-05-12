package org.saturnclient.ui2.components.inputs;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.font.TextRenderer;

public class FloatInput extends Element {
    String text = "";
    public Property<Float> prop;
    public int cursorPosition = 0;
    private TextRenderer textRenderer = SaturnClient.client.textRenderer;

    public FloatInput(Property<Float> prop) {
        this.width = 120;
        this.height = Fonts.getHeight();
        this.prop = prop;
        this.text = String.valueOf(prop.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        int scrollOffset = getScrollOffset();
        String visibleText = text.substring(scrollOffset);

        int newCursorPos = scrollOffset;
        int textWidth = 0;

        for (int i = 0; i < visibleText.length(); i++) {
            textWidth = textRenderer.getWidth(visibleText.substring(0, i + 1));

            if (textWidth > mouseX) {
                break;
            }

            newCursorPos++;
        }

        cursorPosition = newCursorPos;
    }

    @Override
    public void charTyped(char chr) {
        if ((Character.isDigit(chr) || chr == '.') && text.length() < 10) {
            if (text.equals("0") && cursorPosition == 1) {
                text = "" + chr;
            } else {
                text = text.substring(0, cursorPosition) +
                        chr +
                        text.substring(cursorPosition);

                prop.value = text.isEmpty() ? 0 : Float.parseFloat(text);
                ConfigManager.save();

                cursorPosition++;
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (cursorPosition > 0) {
                text = text.substring(0, cursorPosition - 1) +
                        text.substring(cursorPosition);
                cursorPosition--;
                if (text.length() == 0) {
                    text = "0";
                    cursorPosition = 1;
                }
                prop.value = Float.parseFloat(text);
                ConfigManager.save();
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
    public void render(RenderScope renderScope, ElementContext ctx) {
        if (prop.isReset) {
            this.text = String.valueOf(prop.value);
            this.cursorPosition = 0;
            prop.isReset = false;
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, 10, 0xFF000000);

        int scrollOffset = getScrollOffset();
        String visibleText = getVisibleText(scrollOffset);
        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        renderScope.drawText(visibleText, 2, 2, false, textColor);

        if (focused) {
            int cursorX = 2 +
                    textRenderer.getWidth(
                            SaturnUi.text(
                                    visibleText.substring(0, cursorPosition - scrollOffset)));

            renderScope.drawRect(cursorX, 2, 1, height - 1, 0xFFFFFFFF);
        }
    }

    private int getScrollOffset() {
        if (text.isEmpty()) {
            return 0;
        }

        int offset = 0;
        int maxWidth = width - 4; // Available width for text
        while (offset < cursorPosition) { // Ensure cursor remains visible
            String visibleText = text.substring(offset, cursorPosition);
            if (textRenderer.getWidth(SaturnUi.text(visibleText)) > maxWidth) {
                offset++;
            } else {
                break;
            }
        }
        return offset;
    }

    private String getVisibleText(int scrollOffset) {
        String visibleText = text.substring(scrollOffset);

        // Ensure text does not overflow past the available width
        for (int i = 1; i <= visibleText.length(); i++) {
            if (textRenderer.getWidth(
                    SaturnUi.text(visibleText.substring(0, i))) > width - 4) {
                return visibleText.substring(0, i - 1); // Trim overflow
            }
        }
        return visibleText;
    }
}
