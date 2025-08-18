package org.saturnclient.ui2.components.inputs;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

public class Toggle extends Element {
    private static ThemeManager theme = new ThemeManager("Toggle", "hovering", "enabled");
    private static Property<Integer> bgColor = theme.property("bg-color", Property.color(0xFF808080));
    private static Property<Integer> fgColor = theme.property("fg-color", Property.color(0xFF909090));
    private static Property<Integer> cornerRadius = theme.property("corner-radius", Property.integer(30));

    static {
        theme.propertyStateDefault("enabled", "bg-color", 0xaf8B5CF6);
        theme.propertyStateDefault("enabled", "fg-color", 0xff8B5CF6);
    }

    private Property<Boolean> prop;

    public Toggle(Property<Boolean> prop) {
        this.prop = prop;
        this.width = 60;
        this.height = 30;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        theme.setState(null);

        if (prop.value) {
            theme.setState("enabled");
        }

        if (ctx.isHovering()) {
            theme.applyState("hovering");
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        renderScope.drawRoundedRectangle(prop.value ? 30 : 0, 0, 30, 30, 30, fgColor.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        prop.value = !prop.value;
    }
}
