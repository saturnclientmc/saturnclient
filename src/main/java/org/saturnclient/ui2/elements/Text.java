package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;

public class Text extends Element {
    private static ThemeManager theme = new ThemeManager("Text");
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));

    private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        renderScope.drawText(this.text, this.x, this.y, false, fgColor.value);
    }
}
