package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.util.Identifier;

public class Button extends Element {
    private static ThemeManager theme = new ThemeManager("Button", "testing");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(0xFF000000));
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));
    private static Property<Boolean> bold = theme.property("fg-bold", new Property<Boolean>(false));

    static {
        theme.propertyStateDefault("hovering", "fg-color", 0xFFFF0000);
    }

    private String text;
    private Identifier font;

    public Button(String text) {
        this.text = text;
        this.font = Fonts.getFont(bold.value);
        this.dimensions(Fonts.getWidth(text, font)+ 25 , Fonts.getHeight() + 10);
    }

    @Override
    public void render(RenderScope renderScope, boolean hovering) {
        if (hovering) {
            theme.setState("hovering");
        } else {
            theme.setState(null);
        }
        renderScope.drawRoundedRectangle(0, 0, width, height, 10, bgColor.value);

        renderScope.drawText(text, width / 2 - Fonts.getWidth(text, font) / 2, height / 2 - Fonts.getHeight() / 2, bold.value, fgColor.value);
    }
}
