package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class Button extends Element {
    private static ThemeManager theme = new ThemeManager("Button", "hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(0xFF000000));
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));
    private static Property<Integer> cornerRadius = theme.property("corner-radius", new Property<Integer>(10));
    private static Property<Boolean> bold = theme.property("fg-bold", new Property<Boolean>(false));

    static {
        theme.propertyStateDefault("hovering", "fg-color", 0xFF845eee);
    }

    private String text;
    private Runnable onClick;

    public Button(String text, Runnable onClick) {
        this.text = text;
        this.onClick = onClick;
        this.dimensions(Fonts.getWidth(text, bold.value) + 50, 38);
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        if (ctx.isHovering()) {
            theme.setState("hovering");
        } else {
            theme.setState(null);
        }
        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        // renderScope.drawText(text, (width - Fonts.getWidth(text, font)) / 2, (height - Fonts.getHeight()) / 2, bold.value, fgColor.value);
        renderScope.drawText(text, Fonts.centerX(width, text, bold.value), Fonts.centerY(height), bold.value, fgColor.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
