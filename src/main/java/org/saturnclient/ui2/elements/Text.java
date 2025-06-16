package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

public class Text extends Element {
    private static ThemeManager theme = new ThemeManager("Text");
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));
    public static Property<Integer> font = theme.property("font", Property.font(1));

    private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawText(this.text, 0, 0, font.value, fgColor.value);
    }
}
