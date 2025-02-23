package org.auraclient.auraclient.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AuraWidget {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public MinecraftClient client;

    public int background = 0x5F000000;
    public int foreground = 0xFFFFFFFF;

    AuraWidget(int x, int y, int width, int height) {
        x1 = x;
        y1 = y;

        x2 = x + width;
        y2 = y + height;
    }

    void render(DrawContext context, boolean hover) {}

    void mouseClicked() {}
}
