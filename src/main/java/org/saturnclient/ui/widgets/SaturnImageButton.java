package org.saturnclient.ui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnImageButton extends SaturnButton {
    public Identifier sprite;
    public int imageWidth;
    public int imageHeight;

    public SaturnImageButton(Identifier sprite, int imageWidth, int imageHeight, Runnable onPress) {
        super("", onPress);
        this.sprite = sprite;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public void render(DrawContext context, boolean hovering) {
        context.drawGuiTexture(RenderLayer::getGuiTextured, SaturnButton.TEXTURES.get(this.active, hovering), this.x,
                this.y, this.width, this.height, ColorHelper.getWhite(this.alpha));

        context.drawTexture(RenderLayer::getGuiTextured, sprite, x + (width - imageWidth) / 2,
                y + (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight, imageWidth,
                imageHeight, ColorHelper.getWhite(alpha));
    }

    @Override
    public void click() {
        this.onPress.run();
    }
}
