package org.saturnclient.ui2.elements;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.SvgTexture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ImageTexture extends Element {
    Identifier sprite;

    public ImageTexture(Identifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        if (sprite.toString().endsWith(".svg")) {
            MinecraftClient client = MinecraftClient.getInstance();

            // Get the actual window pixel dimensions for the image
            int renderWidth = (int) (width * client.getWindow().getFramebufferWidth() / client.getWindow().getWidth());
            int renderHeight = (int) (height * client.getWindow().getFramebufferHeight() / client.getWindow().getHeight());

            sprite = SvgTexture.getSvg(client, sprite, renderWidth * 2, renderHeight * 2);
        }

        renderScope.drawTexture(sprite, 0, 0, 0, 0, width, height);
    }
}
