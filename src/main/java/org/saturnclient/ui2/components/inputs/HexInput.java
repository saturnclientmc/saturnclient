package org.saturnclient.ui2.components.inputs;

import java.awt.Color;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

public class HexInput extends Element {
    public Property<Integer> prop;

    // Picker state
    private boolean open = false;
    private float hue = 0f;
    private float opacity = 1f;
    private int selectedX = 0;
    private int selectedY = 0;

    private boolean draggingOpacity = false;

    // Dimensions
    private static final int PICKER_WIDTH = 200;
    private static final int PICKER_HEIGHT = 200;
    private static final int SLIDER_WIDTH = 200;
    private static final int SLIDER_HEIGHT = 10;
    private static final int HUE_SLIDER_HEIGHT = 10;

    public HexInput(Property<Integer> prop) {
        this.prop = prop;
        this.width = 58;
        this.height = 20;
        syncFromColor();
    }

    @Override
    public void click(int mouseX, int mouseY) {
        open = !open;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (open && mouseX >= width + 8 && mouseX <= width && mouseY >= 0 && mouseY <= height) {
            handlePickerClick(mouseX - width, mouseY);
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (open)
            handlePickerDrag(mouseX - width, mouseY);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        draggingOpacity = false;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        // Draw the main color rectangle
        renderScope.drawRoundedRectangle(0, 0, width, height, 6, prop.value);

        if (!open)
            return;

        // Draw the picker relative to (width + 8, 0)
        int pickerX = width + 8;
        int pickerY = 0;

        renderColorSquare(renderScope, pickerX, pickerY);
        renderOpacitySlider(renderScope, pickerX, pickerY - SLIDER_HEIGHT - 10);
        renderHueSlider(renderScope, pickerX, pickerY + PICKER_HEIGHT + 10);
    }

    private void renderColorSquare(RenderScope renderScope, int xStart, int yStart) {
        for (int x = 0; x < PICKER_WIDTH; x++) {
            for (int y = 0; y < PICKER_HEIGHT; y++) {
                float saturation = x / (float) PICKER_WIDTH;
                float value = 1f - y / (float) PICKER_HEIGHT;
                int color = Color.HSBtoRGB(hue, saturation, value);
                int alpha = (int) (opacity * 255) & 0xFF;
                color = (alpha << 24) | (color & 0xFFFFFF);
                renderScope.fill(xStart + x, yStart + y, xStart + x + 1, yStart + y + 1, color);
            }
        }

        // Draw selection square
        int selX = xStart + selectedX - 4;
        int selY = yStart + selectedY - 4;
        renderScope.fill(selX, selY, selX + 8, selY + 8, 0xFF000000 | prop.value);
        renderScope.drawBorder(selX, selY, 8, 8, 0xFFFFFFFF);
    }

    private void renderOpacitySlider(RenderScope renderScope, int xStart, int yStart) {
        for (int x = 0; x < SLIDER_WIDTH; x++) {
            float localOpacity = x / (float) SLIDER_WIDTH;
            int alpha = (int) (localOpacity * 255) & 0xFF;
            int color = (alpha << 24) | (prop.value & 0xFFFFFF);
            renderScope.fill(xStart + x, yStart, xStart + x + 1, yStart + SLIDER_HEIGHT, color);
        }

        int knobX = xStart + (int) (opacity * SLIDER_WIDTH);
        renderScope.fill(knobX - 1, yStart - 2, knobX + 1, yStart + SLIDER_HEIGHT + 2, 0xFF000000);
    }

    private void renderHueSlider(RenderScope renderScope, int xStart, int yStart) {
        for (int x = 0; x < SLIDER_WIDTH; x++) {
            float localHue = x / (float) SLIDER_WIDTH;
            int color = Color.HSBtoRGB(localHue, 1f, 1f);
            renderScope.fill(xStart + x, yStart, xStart + x + 1, yStart + HUE_SLIDER_HEIGHT, 0xFF000000 | color);
        }

        int knobX = xStart + (int) (hue * SLIDER_WIDTH);
        renderScope.fill(knobX - 1, yStart - 2, knobX + 1, yStart + HUE_SLIDER_HEIGHT + 2, 0xFF000000);
    }

    private void handlePickerClick(double mouseX, double mouseY) {
        // Color square
        if (mouseX >= 0 && mouseX < PICKER_WIDTH && mouseY >= 0 && mouseY < PICKER_HEIGHT) {
            selectedX = (int) mouseX;
            selectedY = (int) mouseY;
            updateColorFromSelection();
        }

        // Opacity slider
        int opY = -SLIDER_HEIGHT - 10;
        if (mouseX >= 0 && mouseX < SLIDER_WIDTH && mouseY >= opY && mouseY <= opY + SLIDER_HEIGHT) {
            updateOpacity(mouseX);
            draggingOpacity = true;
        }

        // Hue slider
        int hueY = PICKER_HEIGHT + 10;
        if (mouseX >= 0 && mouseX < SLIDER_WIDTH && mouseY >= hueY && mouseY <= hueY + HUE_SLIDER_HEIGHT) {
            updateHue(mouseX);
        }
    }

    private void handlePickerDrag(double mouseX, double mouseY) {
        // Opacity
        if (draggingOpacity)
            updateOpacity(mouseX);

        // Color square
        if (mouseX >= 0 && mouseX < PICKER_WIDTH && mouseY >= 0 && mouseY < PICKER_HEIGHT) {
            selectedX = (int) mouseX;
            selectedY = (int) mouseY;
            updateColorFromSelection();
        }
    }

    private void updateColorFromSelection() {
        float saturation = selectedX / (float) PICKER_WIDTH;
        float value = 1f - selectedY / (float) PICKER_HEIGHT;
        int color = Color.HSBtoRGB(hue, saturation, value);
        int alpha = (int) (opacity * 255) & 0xFF;
        prop.value = (alpha << 24) | (color & 0xFFFFFF);
    }

    private void updateHue(double mouseX) {
        hue = (float) Math.max(0, Math.min(1, mouseX / SLIDER_WIDTH));
        updateColorFromSelection();
    }

    private void updateOpacity(double mouseX) {
        opacity = (float) Math.max(0, Math.min(1, mouseX / SLIDER_WIDTH));
        updateColorFromSelection();
    }

    private void syncFromColor() {
        int rgb = prop.value & 0xFFFFFF;
        float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, null);
        hue = hsb[0];
        selectedX = (int) (hsb[1] * PICKER_WIDTH);
        selectedY = (int) ((1f - hsb[2]) * PICKER_HEIGHT);
        int alpha = (prop.value >> 24) & 0xFF;
        if (alpha == 0)
            alpha = 255;
        opacity = alpha / 255f;
    }
}