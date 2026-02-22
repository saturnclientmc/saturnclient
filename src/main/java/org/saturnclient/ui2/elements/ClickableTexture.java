package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ClickableTexture extends Element {
    Identifier sprite;
    private Runnable onClick;

    public ClickableTexture(Identifier sprite, Runnable onClick) {
        this.sprite = sprite;
        this.onClick = onClick;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);
        renderScope.drawTexture(sprite, 0, 0, 0, 0, width, height,
                ctx.isHovering() ? Theme.ACCENT.value : Theme.PRIMARY.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
