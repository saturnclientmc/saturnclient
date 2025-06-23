package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;

import net.minecraft.util.Identifier;

public class TabMenu extends Element {
    private static ThemeManager theme = new ThemeManager("TabMenu", "hovering", "selected");
    private static Property<Integer> bgColor = theme.property("bg-color", Property.color(0x90000000));
    private static Property<Integer> iconFg = theme.property("icon-fg", Property.color(0xFFFFFFFF));
    private static Property<Integer> iconBg = theme.property("icon-bg", Property.color(0x00000000));
    private static Property<Integer> cornerRadius = theme.property("corner-radius", Property.integer(10));

    static {
        theme.propertyStateDefault("hovering", "icon-bg", -7643914);
    }

    public static class TabMenuComponent {
        Runnable onClick;
        Identifier sprite;

        public TabMenuComponent(Identifier sprite, Runnable onClick) {
            this.onClick = onClick;
            this.sprite = sprite;
        }
    }
    
    TabMenuComponent[] components;
    public int selectedScreen = 0;

    public TabMenu(int active, TabMenuComponent... components) {
        this.components = components;
        width = 25 * components.length + 6;
        height = 30;
        this.selectedScreen = active;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        int col = 0;
        for (TabMenuComponent component : components) {
            int x = 25 * col + 5;
            if (ctx.isHovering(x, 5, 20, 20) || selectedScreen == col) {
                theme.setState("hovering");
            }

            renderScope.drawRoundedRectangle(x, 5, 20, 20, cornerRadius.value, iconBg.value);
            renderScope.drawTexture(component.sprite, x + 5, 10, 0, 0, 10, 10, iconFg.value);

            theme.setState(null);
            col++;
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        int col = 0;
        for (TabMenuComponent component : components) {
            int x = 25 * col + 5;
            if (Utils.isHovering(mouseX - x, mouseY - 5, 20, 20, 1.0f)) {
                component.onClick.run();
            }
            col++;
        }
    }
}
