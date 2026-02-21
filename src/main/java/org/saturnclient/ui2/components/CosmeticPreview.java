package org.saturnclient.ui2.components;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class CosmeticPreview extends Element {
    static {
    }

    Identifier sprite;
    private Runnable onClick;
    private boolean isSelected;

    public CosmeticPreview(boolean isSelected, Identifier sprite, Runnable onClick) {
        this.sprite = sprite;
        this.onClick = onClick;
        this.isSelected = isSelected;

        this.width = 50;
        this.height = 111;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, Theme.WIDGET_RADIUS.value,
                isSelected ? Theme.ACCENT.value : Theme.PRIMARY.value);

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        int texWidth = width - Theme.PADDING.value;
        int texHeight = height - Theme.PADDING.value;

        renderScope.drawTexture(sprite, Theme.PADDING.value / 2, Theme.PADDING.value / 2, 0, 0, texWidth, texHeight,
                Theme.FOREGROUND.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
