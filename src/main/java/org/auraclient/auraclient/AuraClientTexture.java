package org.auraclient.auraclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class AuraClientTexture implements Drawable {
    // Textures
    public static Identifier BOX_1_1 = Identifier.of("auraclient", "textures/gui/box_1_1.png");

    Identifier sprite;
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    boolean focused = false; 

    public AuraClientTexture(Identifier sprite) {
        this.sprite = sprite;
    }

    public static AuraClientTexture builder(Identifier sprite) {
        return new AuraClientTexture(sprite);
    }

    public AuraClientTexture dimensions(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderLayer::getGuiTextured, sprite, x, y, 0, 0, width, height, width, height);
    }
}
