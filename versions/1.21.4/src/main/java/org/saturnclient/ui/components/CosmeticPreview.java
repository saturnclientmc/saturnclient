package org.saturnclient.ui.components;

import org.saturnclient.common.minecraft.SaturnIdentifier;
import org.saturnclient.config.Theme;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class CosmeticPreview extends Element {
    static int padding = 14;

    Identifier sprite;
    private Runnable onClick;
    private boolean isSelected;

    public CosmeticPreview(boolean isSelected, SaturnIdentifier sprite, Runnable onClick) {
        this.sprite = (Identifier) sprite.id;
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

        int texWidth = width - padding;
        int texHeight = height - padding;

        renderScope.drawTexture(sprite, padding / 2, padding / 2, 0, 0, texWidth, texHeight,
                Theme.FOREGROUND.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
