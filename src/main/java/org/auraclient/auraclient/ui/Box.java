package org.auraclient.auraclient.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class Box extends AuraWidget {
    public static Identifier BOX = Identifier.of("auraclient", "textures/gui/box/box.png");

    public Box(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void render(DrawContext context, boolean hover) {
        int width = x2 - x1;
        int height = y2 - y1;
        context.drawTexture(RenderLayer::getGuiTextured, Box.BOX, x1, y1, 0, 0, width, height, width, height);
    }
}
