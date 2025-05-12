package org.saturnclient.ui2.components;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.screens.CloakMenu;
import org.saturnclient.ui2.screens.HatMenu;
import org.saturnclient.ui2.screens.ModMenu;
import org.saturnclient.ui2.screens.SaturnConfigEditor;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class Sidebar extends Element {
    private static ThemeManager theme = new ThemeManager("SideBar", "hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(0x90000000));
    private static Property<Integer> iconFg = theme.property("icon-fg", new Property<Integer>(-1));
    private static Property<Integer> iconBg = theme.property("icon-bg", new Property<Integer>(0x00000000));

    private static Property<Integer> cornerRadius = theme.property("corner-radius", new Property<Integer>(10));
    private static Property<Integer> iconRadius = theme.property("icon-radius", new Property<Integer>(10));
    private static Property<Integer> padding = theme.property("padding", new Property<Integer>(10));

    static {
        theme.propertyStateDefault("hovering", "icon-bg", -7643914);
    }

    public static class SidebarComponent {
        Runnable onClick;
        boolean end;
        Identifier sprite;

        public SidebarComponent(Identifier sprite, Runnable onClick, boolean end) {
            this.onClick = onClick;
            this.end = end;
            this.sprite = sprite;
        }
    }

    Runnable onClose;
    SidebarComponent[] components = {
        new Sidebar.SidebarComponent(Textures.MODS_TAB, () -> {
                SaturnClient.client.setScreen(new ModMenu());
            }, false),
            new Sidebar.SidebarComponent(Textures.SETTINGS, () -> {
                SaturnClient.client.setScreen(new SaturnConfigEditor());
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOAK, () -> {
                SaturnClient.client.setScreen(new CloakMenu());
            }, false),

            new Sidebar.SidebarComponent(Textures.HAT, () -> {
                SaturnClient.client.setScreen(new HatMenu());
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOSE, () -> {
                onClose.run();
            }, true)
    };
    int active = 0;

    public Sidebar(int active, Runnable onClose) {
        this.width = 30;
        this.height = 350;
        this.active = active;
        this.onClose = onClose;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        int topRow = 0;
        int bottomRow = 0;

        int sp = 30 - padding.value;
        int s = 30 - (padding.value * 2);

        int s2 = s + padding.value;

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        int i = 0;

        for (SidebarComponent component : components) {
            if (component.end) {
                int y = (height - (sp * bottomRow)) - (s * 2);
                if (ctx.isHovering(0, y - (padding.value / 2), s2, s2) || active == i) {
                    theme.setState("hovering");
                } else {
                    theme.setState(null);
                }
                renderScope.drawRoundedRectangle(padding.value / 2, y - (padding.value / 2), s + padding.value, s + padding.value, iconRadius.value, iconBg.value);
                renderScope.drawTexture(component.sprite, padding.value, y, 0, 0, s, s, iconFg.value);
                bottomRow += 1;
            } else {
                int y = padding.value + ((sp) * topRow);
                if (ctx.isHovering(0, y - (padding.value / 2), s2, s2) || active == i) {
                    theme.setState("hovering");
                } else {
                    theme.setState(null);
                }
                renderScope.drawRoundedRectangle(padding.value / 2, y - (padding.value / 2), s + padding.value, s + padding.value, iconRadius.value, iconBg.value);
                renderScope.drawTexture(component.sprite, padding.value, y, 0, 0, s, s, iconFg.value);
                topRow += 1;
            }

            i++;
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        int topRow = 0;
        int bottomRow = 0;

        int sp = 30 - padding.value;
        int s = 30 - (padding.value * 2);
        int s2 = s + padding.value;

        for (SidebarComponent component : components) {
            if (component.end) {
                if (Utils.isHovering(mouseX, mouseY - (height - (sp * bottomRow) - (s * 2)), s2, s2, scale)) {
                    component.onClick.run();
                    break;
                }
                bottomRow += 1;
            } else {
                if (Utils.isHovering(mouseX, mouseY - (padding.value + ((sp) * topRow)), s2, s2, scale)) {
                    component.onClick.run();
                    break;
                }
                topRow += 1;
            }
        }
    }
}
