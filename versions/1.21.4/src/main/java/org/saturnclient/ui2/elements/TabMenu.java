package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;

import net.minecraft.util.Identifier;

public class TabMenu extends Element {
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
        renderScope.drawRoundedRectangle(0, 0, width, height, Theme.WIDGET_RADIUS.value, Theme.PRIMARY.value);

        int col = 0;
        for (TabMenuComponent component : components) {
            int x = 25 * col + 5;

            boolean hovering = ctx.isHovering(x, 5, 20, 20) || selectedScreen == col;

            if (hovering) {
                renderScope.drawRoundedRectangle(x, 5, 20, 20, Theme.WIDGET_RADIUS.value, Theme.ACCENT.value);
            }

            renderScope.drawTexture(component.sprite, x + 5, 10, 0, 0, 10, 10,
                    hovering ? Theme.ACCENT_FG.value : Theme.PRIMARY_FG.value);

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
