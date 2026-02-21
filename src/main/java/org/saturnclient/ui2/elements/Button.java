package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class Button extends Element {
    private String text;
    private Runnable onClick;

    public Button(String text, Runnable onClick) {
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, Theme.WIDGET_RADIUS.value,
                ctx.isHovering() ? Theme.ACCENT.value : Theme.PRIMARY.value);

        renderScope.drawText(text, Fonts.centerX(width, text, Theme.FONT.value), Fonts.centerY(height),
                Theme.FONT.value,
                ctx.isHovering() ? Theme.ACCENT_FG.value : Theme.PRIMARY_FG.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
