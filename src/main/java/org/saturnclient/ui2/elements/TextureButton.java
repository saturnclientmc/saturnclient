package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class TextureButton extends Element {
    Identifier sprite;
    private Runnable onClick;
    static int padding = 14;

    public TextureButton(Identifier sprite, Runnable onClick) {
        this.sprite = sprite;
        this.onClick = onClick;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, Theme.WIDGET_RADIUS.value,
                ctx.isHovering() ? Theme.ACCENT.value : Theme.PRIMARY.value);

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        int texWidth = width - padding;
        int texHeight = height - padding;

        renderScope.drawTexture(sprite, padding / 2, padding / 2, 0, 0, texWidth, texHeight,
                ctx.isHovering() ? Theme.ACCENT_FG.value : Theme.PRIMARY_FG.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
