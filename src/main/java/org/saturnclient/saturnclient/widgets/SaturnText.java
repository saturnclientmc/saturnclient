package org.saturnclient.saturnclient.widgets;

import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.math.ColorHelper;

/**
 * Represents a drawable text element in the Saturn Client UI.
 * Uses the builder pattern for flexible text creation and configuration.
 */
public class SaturnText implements Drawable {
    private String text;
    private Consumer<SaturnText> onRender;
    public int x;
    public int y;
    public float alpha;

    /**
     * Creates a new text element with the specified string.
     *
     * @param text The text to display
     */
    public SaturnText(String text) {
        this.text = text;
        this.x = 0;
        this.y = 0;
        this.alpha = 1.0f;
        this.onRender = __ -> {
        };
    }

    public SaturnText onRender(Consumer<SaturnText> onRender) {
        this.onRender = onRender;
        return this;
    }

    public SaturnText setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public SaturnText setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Creates a new builder instance for the text.
     *
     * @param text The text content
     * @return A new SaturnText instance
     */
    public static SaturnText builder(String text) {
        return new SaturnText(text);
    }

    /**
     * Sets the position of the text.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return This instance for method chaining
     */
    public SaturnText position(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        onRender.accept(this);
        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, ColorHelper.getWhite(alpha), false);
    }
}
