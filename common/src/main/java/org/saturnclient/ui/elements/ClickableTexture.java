package org.saturnclient.ui.elements;

import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.config.Theme;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;

public class ClickableTexture extends Element {
    SaturnIdentifier sprite;
    private Runnable onClick;

    public ClickableTexture(SaturnIdentifier sprite, Runnable onClick) {
        this.sprite = sprite;
        this.onClick = onClick;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
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
