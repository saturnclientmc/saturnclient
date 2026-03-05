package org.saturnclient.ui.elements;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.resources.Fonts;

public class Text extends Element {
    private String text;

    public Text(String text) {
        this.text = text;
        width = Fonts.getWidth(text, Theme.FONT.value);
        height = Fonts.getHeight();
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawText(this.text, 0, 0, Theme.FONT.value, Theme.FOREGROUND.value);
    }
}
