package org.saturnclient.ui.widgets;

import org.saturnclient.ui.SaturnWidget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnImage extends SaturnWidget {
    private final Identifier sprite;

    public SaturnImage(Identifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, sprite, x, y, 0, 0, width, height, width,
                height, ColorHelper.getWhite(alpha));
    }
}
