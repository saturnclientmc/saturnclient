package org.auraclient.auraclient.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AuraWidget {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public MinecraftClient client;

    public static int background = 0x5F000000;
    public static int foreground = 0xFFFFFFFF;

    void render(DrawContext context, boolean hover) {}

    void mouseClicked() {}
}
