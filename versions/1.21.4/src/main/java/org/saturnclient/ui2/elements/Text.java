package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

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
