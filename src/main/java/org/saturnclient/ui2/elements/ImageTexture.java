package org.saturnclient.ui2.elements;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ImageTexture extends Element {
    Identifier sprite;

    public ImageTexture(Identifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);
        renderScope.drawTexture(sprite, 0, 0, 0, 0, width, height);
    }
}
