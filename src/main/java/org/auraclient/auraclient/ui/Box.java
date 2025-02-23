package org.auraclient.auraclient.ui;

import net.minecraft.client.gui.DrawContext;

public class Box extends AuraWidget {
    public Box(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void render(DrawContext context, boolean hover) {
        context.fill(x1, y1, x2, y2, background);
    }
}
