package org.saturnclient.ui.widgets;

import org.saturnclient.ui.SaturnWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;

public class SaturnInputBox extends SaturnWidget {

    private TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private int tick = 0;
    private boolean cursorVisible = true;

    public Property<String> prop;
    public int cursorPosition = 0;
    public Runnable onUpdate;

    public SaturnInputBox(Property<String> prop, int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.prop = prop;
        this.height = textRenderer.fontHeight + 4;
        this.scale = 1.0f;
    }

    @Override
    public void click(int mouseX, int mouseY) {
        int scrollOffset = getScrollOffset();
        String visibleText = prop.value.substring(scrollOffset);

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
        prop.value = prop.value.substring(0, cursorPosition) +
                chr +
                prop.value.substring(cursorPosition);

        cursorPosition++;

        onUpdate.run();
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (cursorPosition > 0) {
                prop.value = prop.value.substring(0, cursorPosition - 1) +
                        prop.value.substring(cursorPosition);
                cursorPosition--;
            }
            onUpdate.run();
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (cursorPosition > 0) {
                cursorPosition--;
            }
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (cursorPosition < prop.value.length()) {
                cursorPosition++;
            }
        }
    }

    @Override
    public void render(
            DrawContext context,
            boolean hovering,
            int mouseX,
            int mouseY) {
        SaturnUi.drawHighResGuiTexture(
                context,
                Textures.BUTTON,
                0,
                0,
                this.width,
                this.height,
                focused
                        ? SaturnClientConfig.COLOR.value
                        : SaturnClientConfig.getWhite(this.alpha));

        if (hovering || focused) {
            SaturnUi.drawHighResGuiTexture(
                    context,
                    Textures.BUTTON_BORDER,
                    0,
                    0,
                    this.width,
                    this.height,
                    hovering
                            ? SaturnClientConfig.COLOR.value
                            : SaturnClientConfig.getWhite(this.alpha));
        }

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        if (prop.value.isEmpty() && !focused) {
            SaturnUi.drawHighResTexture(
                    context,
                    Textures.SEARCH,
                    3,
                    3,
                    9,
                    9,
                    hovering
                            ? SaturnClientConfig.COLOR.value
                            : SaturnClientConfig.getWhite(this.alpha));

            matrices.scale(0.9f, 0.9f, 1.0f);

            context.drawText(
                    textRenderer,
                    SaturnUi.text("Search"),
                    14,
                    textRenderer.fontHeight / 2 + 1,
                    hovering
                            ? SaturnClientConfig.COLOR.value
                            : SaturnClientConfig.getWhite(this.alpha),
                    false);
        }

        matrices.pop();

        matrices.push();

        matrices.scale(0.9f, 0.9f, 1.0f);

        int scrollOffset = getScrollOffset();
        String visibleText = getVisibleText(scrollOffset);
        int textColor = focused ? 0xFFFFFF : 0xAAAAAA;
        context.drawText(
                textRenderer,
                SaturnUi.text(visibleText),
                3,
                textRenderer.fontHeight / 2,
                textColor,
                false);

        if (focused && cursorVisible) {
            int cursorX = 2 +
                    textRenderer.getWidth(
                            SaturnUi.text(
                                    visibleText.substring(0, cursorPosition - scrollOffset)));
            matrices.translate(cursorX, 0, 0);
            context.fill(
                    1,
                    2,
                    2,
                    textRenderer.fontHeight + 3,
                    0xFFFFFFFF);
        }

        matrices.pop();
    }

    public void clearText() {
        prop.value = "";
        cursorPosition = 1;
    }

    private int getScrollOffset() {
        if (prop.value.isEmpty()) {
            return 0;
        }

        int offset = 0;
        int maxWidth = width - 4; // Available width for prop.value
        while (offset < cursorPosition) { // Ensure cursor remains visible
            String visibleText = prop.value.substring(offset, cursorPosition);
            if (textRenderer.getWidth(SaturnUi.text(visibleText)) > maxWidth) {
                offset++;
            } else {
                break;
            }
        }
        return offset;
    }

    private String getVisibleText(int scrollOffset) {
        String visibleText = prop.value.substring(scrollOffset);

        // Ensure prop.value does not overflow past the available width
        for (int i = 1; i <= visibleText.length(); i++) {
            if (textRenderer.getWidth(
                    SaturnUi.text(visibleText.substring(0, i))) > width - 4) {
                return visibleText.substring(0, i - 1); // Trim overflow
            }
        }
        return visibleText;
    }

    public SaturnInputBox onUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    @Override
    public void tick() {
        if (focused) {
            if (tick < 45 || tick < 60 && cursorVisible) {
                tick++;
            } else {
                tick = 0;
                cursorVisible = !cursorVisible;
            }
        }
    }
}
