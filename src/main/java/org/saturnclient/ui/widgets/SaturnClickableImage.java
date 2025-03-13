package org.saturnclient.ui.widgets;

import org.saturnclient.saturnclient.SaturnClient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnClickableImage extends SaturnButton {
    public Identifier sprite;
    public boolean selected = false;

    public SaturnClickableImage(Identifier sprite, Runnable onPress) {
        super("", onPress);
        this.sprite = sprite;
    }

    @Override
    public void render(DrawContext context, boolean hovering) {
        context.drawTexture(RenderLayer::getGuiTextured, sprite, x, y, 0, 0, width, height, width,
                height, hovering || selected ? SaturnClient.COLOR : ColorHelper.getWhite(alpha));
    }

    @Override
    public void click() {
        this.onPress.run();
    }

    public SaturnClickableImage setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public SaturnClickableImage setBackground(boolean background) {
        this.background = background;
        return this;
    }
}
