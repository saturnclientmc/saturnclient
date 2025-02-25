package org.auraclient.auraclient.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class AuraWidget {
    public int x;
    public int y;
    public int width;
    public int height;
    public MinecraftClient client;
    public Identifier sprite;
    public Runnable onClick;

    public AuraWidget(Identifier sprite, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.onClick = () -> {};
    }

    public AuraWidget(Identifier sprite, int x, int y, int width, int height, Runnable onClick) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.onClick = onClick;
    }

    void render(DrawContext context, boolean hover) {
        context.drawTexture(RenderLayer::getGuiTextured, sprite, x, y, 0, 0, width, height, width, height);
    }
}
