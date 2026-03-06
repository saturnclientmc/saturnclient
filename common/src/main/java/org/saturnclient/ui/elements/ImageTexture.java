package org.saturnclient.ui.elements;

import org.saturnclient.common.minecraft.IMinecraftClient;
import org.saturnclient.common.minecraft.MinecraftProvider;
import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;

public class ImageTexture extends Element {
    SaturnIdentifier sprite;

    public ImageTexture(SaturnIdentifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        // if (sprite.toString().endsWith(".svg")) {
        //     IMinecraftClient client = MinecraftProvider.PROVIDER.getClient();

        //     // Get the actual window pixel dimensions for the image
        //     int renderWidth = (int) (width * client.getWindow().getFramebufferWidth() / client.getWindow().getWidth());
        //     int renderHeight = (int) (height * client.getWindow().getFramebufferHeight()
        //             / client.getWindow().getHeight());

        //     sprite = SvgTexture.getSvg(client, sprite, renderWidth * 2, renderHeight * 2);
        // }

        renderScope.drawTexture(sprite, 0, 0, 0, 0, width, height);
    }
}
