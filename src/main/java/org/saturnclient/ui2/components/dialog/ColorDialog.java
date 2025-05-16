package org.saturnclient.ui2.components.dialog;

import java.awt.Color;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.SaturnScreen;

import net.minecraft.client.gui.DrawContext;

public class ColorDialog extends SaturnScreen {
    private Property<Integer> prop;
    private SaturnScreen parent;
    private static final int PICKER_WIDTH = 200;
    private static final int PICKER_HEIGHT = 200;
    private static final int SLIDER_WIDTH = 200;
    private static final int SLIDER_HEIGHT = 10;
    private static final int HUE_SLIDER_HEIGHT = 10;
    private float hue = 0.0f;
    
    private int selectedX = 0;
    private int selectedY = 0;

    private float opacity = 1.0f;
    private boolean dragging = false;

    public ColorDialog(Property<Integer> prop , SaturnScreen parent) {
        super("Color Dialog");
        this.prop = prop;
        this.parent = parent;

        int alpha = (prop.value >> 24) & 0xFF;
        if (alpha == 0) alpha = 255; // fallback if no alpha specified
        this.opacity = alpha / 255.0f;
        syncFromColor();
    }

    @Override
    public void ui() {}

    @Override
    public void close() {
        SaturnClient.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderScope renderScope = new RenderScope(context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        renderScope.matrices.push();

        renderScope.matrices.scale(0.5f, 0.5f, 0.5f);

        int xStart = (width - PICKER_WIDTH) / 2;
        int yStart = (height - PICKER_HEIGHT) / 2;

        // Draw the HSV saturation/value square (with current hue)
        for (int x = 0; x < PICKER_WIDTH; x++) {
            for (int y = 0; y < PICKER_HEIGHT; y++) {
                float saturation = x / (float) PICKER_WIDTH;
                float value = 1.0f - y / (float) PICKER_HEIGHT;
                int color = Color.HSBtoRGB(hue, saturation, value);
                context.fill(xStart + x, yStart + y, xStart + x + 1, yStart + y + 1, 0xFF000000 | color);
            }
        }

        int x = (xStart + selectedX) - 4;
        int y = (yStart + selectedY) - 4;
        context.fill(x, y, x+8, y+8, 0xFF000000 | prop.value);
        context.drawBorder(x, y, 8, 8, 0xFFffffff);

        renderSlider(context, mouseX, mouseY, delta);
        renderHueSlider(context);

        renderScope.matrices.pop();
    }

    public void renderSlider(DrawContext context, int mouseX, int mouseY, float delta) {
        int xStart = (width - SLIDER_WIDTH) / 2;
        int yStart = (height - SLIDER_HEIGHT - PICKER_HEIGHT) / 2 - 10;

        // Draw slider background (left = transparent, right = white)
        for (int x = 0; x < SLIDER_WIDTH; x++) {
            float localOpacity = x / (float) SLIDER_WIDTH;
            int alpha = (int)(localOpacity * 255) & 0xFF;
            int color = (alpha << 24) | (prop.value & 0xFFFFFF);
            context.fill(xStart + x, yStart, xStart + x + 1, yStart + SLIDER_HEIGHT, color);
        }

        // Draw current value as a marker (thin black line)
        int knobX = xStart + (int)(opacity * SLIDER_WIDTH);
        context.fill(knobX - 1, yStart - 2, knobX + 1, yStart + SLIDER_HEIGHT + 2, 0xFF000000);

        // Draw opacity text
        // context.drawText(textRenderer, String.format("Opacity: %.2f", opacity), xStart, yStart - 15, 0xFFFFFF, false);
    }

    private void renderHueSlider(DrawContext context) {
        int xStart = (width - SLIDER_WIDTH) / 2;
        int yStart = (height + PICKER_HEIGHT) / 2 + 10;

        for (int x = 0; x < SLIDER_WIDTH; x++) {
            float localHue = x / (float) SLIDER_WIDTH;
            int color = Color.HSBtoRGB(localHue, 1.0f, 1.0f);
            context.fill(xStart + x, yStart, xStart + x + 1, yStart + HUE_SLIDER_HEIGHT, 0xFF000000 | color);
        }

        int knobX = xStart + (int)(hue * SLIDER_WIDTH);
        context.fill(knobX - 1, yStart - 2, knobX + 1, yStart + HUE_SLIDER_HEIGHT + 2, 0xFF000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;

        int xStart = (width - PICKER_WIDTH) / 2;
        int yStart = (height - PICKER_HEIGHT) / 2;

        int relX = (int) mouseX - xStart;
        int relY = (int) mouseY - yStart;

        if (relX >= 0 && relX < PICKER_WIDTH && relY >= 0 && relY < PICKER_HEIGHT) {
            float saturation = relX / (float) PICKER_WIDTH;
            float value = 1.0f - relY / (float) PICKER_HEIGHT;
            prop.value = Color.HSBtoRGB(hue, saturation, value);

            int alpha = (int)(opacity * 255) & 0xFF;
            prop.value = (alpha << 24) | (prop.value & 0xFFFFFF);

            selectedX = relX;
            selectedY = relY;

            return true;
        } else if (isInSlider(mouseX, mouseY)) {
            updateOpacity(mouseX);
            dragging = true;
            return true;
        } else if (isInHueSlider(mouseX, mouseY)) {
            updateHue(mouseX);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        mouseX *= 2;
        mouseY *= 2;

        if (isInHueSlider(mouseX, mouseY)) {
            updateHue(mouseX);
            return true;
        } else if (dragging) {
            updateOpacity(mouseX);
            return true;
        } else {
            int xStart = (width - PICKER_WIDTH) / 2;
            int yStart = (height - PICKER_HEIGHT) / 2;

            int relX = (int) mouseX - xStart;
            int relY = (int) mouseY - yStart;

            if (relX >= 0 && relX < PICKER_WIDTH && relY >= 0 && relY < PICKER_HEIGHT) {
                float saturation = relX / (float) PICKER_WIDTH;
                float value = 1.0f - relY / (float) PICKER_HEIGHT;
                prop.value = Color.HSBtoRGB(hue, saturation, value);

                int alpha = (int)(opacity * 255) & 0xFF;
                prop.value = (alpha << 24) | (prop.value & 0xFFFFFF);

                selectedX = relX;
                selectedY = relY;

                return true;
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private boolean isInSlider(double mouseX, double mouseY) {
        int xStart = (width - SLIDER_WIDTH) / 2;
        int yStart = (height - SLIDER_HEIGHT - PICKER_HEIGHT) / 2 - 10;
        return mouseX >= xStart && mouseX <= xStart + SLIDER_WIDTH &&
               mouseY >= yStart && mouseY <= yStart + SLIDER_HEIGHT;
    }

    private boolean isInHueSlider(double mouseX, double mouseY) {
        int xStart = (width - SLIDER_WIDTH) / 2;
        int yStart = (height + PICKER_HEIGHT) / 2 + 10;
        return mouseX >= xStart && mouseX <= xStart + SLIDER_WIDTH &&
            mouseY >= yStart && mouseY <= yStart + HUE_SLIDER_HEIGHT;
    }

    private void updateOpacity(double mouseX) {
        int xStart = (width - SLIDER_WIDTH) / 2;
        float newOpacity = (float)((mouseX - xStart) / SLIDER_WIDTH);
        opacity = Math.max(0.0f, Math.min(1.0f, newOpacity));
    
        int alpha = (int)(opacity * 255) & 0xFF;
        prop.value = (alpha << 24) | (prop.value & 0xFFFFFF);
    }    

    private void updateHue(double mouseX) {
        int xStart = (width - SLIDER_WIDTH) / 2;
        float newHue = (float)((mouseX - xStart) / SLIDER_WIDTH);
        hue = Math.max(0.0f, Math.min(1.0f, newHue));

        float saturation = selectedX / (float) PICKER_WIDTH;
        float value = 1.0f - selectedY / (float) PICKER_HEIGHT;
        prop.value = Color.HSBtoRGB(hue, saturation, value);

        int alpha = (int)(opacity * 255) & 0xFF;
        prop.value = (alpha << 24) | (prop.value & 0xFFFFFF);
    }

    private void syncFromColor() {
        int rgb = prop.value & 0xFFFFFF;
        float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, null);

        this.hue = hsb[0];

        float saturation = hsb[1];
        float value = hsb[2];

        this.selectedX = (int)(saturation * PICKER_WIDTH);
        this.selectedY = (int)((1.0f - value) * PICKER_HEIGHT);

        // Extract alpha and convert to opacity float
        int alpha = (prop.value >> 24) & 0xFF;
        if (alpha == 0) alpha = 255;
        this.opacity = alpha / 255.0f;
    }
}
