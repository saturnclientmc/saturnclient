package org.auraclient.auraclient.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ImageButton extends AuraWidget {
    private final Identifier sprite;
    public int hoverBorder = 0xFFFFFFFF;
    public Runnable onClick;

    public ImageButton(Identifier sprite_, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = sprite_;
        this.onClick = () -> {};
    }

    public ImageButton(Identifier sprite_, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        super(x, y, width, height);
        this.sprite = sprite_;
        this.onClick = () -> {};
    }

    @Override
    public void render(DrawContext context, boolean hover) {
        int width = x2 - x1;
        int height = y2 - y1;

        context.drawTexture(RenderLayer::getGuiTextured, sprite, x1, y1, 0, 0, width, height, width, height);

        if (hover) {
            context.drawBorder(x1 - 1, y1 - 1, x2 - x1 + 2, y2 - y1 + 2, hoverBorder);
        }
    }

    @Override
    void mouseClicked() {
        onClick.run();
    }
}
