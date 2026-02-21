package org.saturnclient.ui2.components;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.SaturnClientConfig;
import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.screens.ConfigEditor;
import org.saturnclient.ui2.screens.ModMenu;
import org.saturnclient.ui2.screens.cosmetics.CloakMenu;
import org.saturnclient.ui2.screens.store.CloakStore;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class Sidebar extends Element {
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
                SaturnClient.client.setScreen(new ConfigEditor(SaturnClientConfig.config));
            }, false),

            new Sidebar.SidebarComponent(Textures.SHIRT, () -> {
                SaturnClient.client.setScreen(new CloakMenu());
            }, false),

            new Sidebar.SidebarComponent(Textures.CLOSE, () -> {
                onClose.run();
            }, true),

            new Sidebar.SidebarComponent(Textures.STORE, () -> {
                SaturnClient.client.setScreen(new CloakStore());
            }, true),
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

        int gap = 5; // 5px gap between icons
        int padding = 10;
        int sp = 30 - padding + gap;
        int s = 30 - (padding * 2);
        int s2 = s + padding;

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        renderScope.drawRoundedRectangle(0, 0, width, height, Theme.BG_RADIUS.value, Theme.BACKGROUND.value);

        int i = 0;

        for (SidebarComponent component : components) {
            int y = component.end ? ((height - (sp * bottomRow)) - (s * 2)) : (padding + (sp * topRow));
            boolean selected = ctx.isHovering(0, y - (padding / 2), s2, s2) || active == i;

            renderScope.drawRoundedRectangle(padding / 2, y - (padding / 2), s + padding,
                    s + padding, Theme.WIDGET_RADIUS.value, Theme.getBg(selected));

            renderScope.drawTexture(component.sprite, padding, y, 0, 0, s, s, Theme.getFg(selected));

            if (component.end) {
                bottomRow += 1;
            } else {
                topRow += 1;
            }

            i++;
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        int topRow = 0;
        int bottomRow = 0;

        int gap = 5;
        int padding = 10;
        int sp = 30 - padding + gap;
        int s = 30 - (padding * 2);
        int s2 = s + padding;

        for (SidebarComponent component : components) {
            int y = component.end ? ((height - (sp * bottomRow)) - (s * 2)) : (padding + (sp * topRow));
            boolean hovering = Utils.isHovering(mouseX - (padding / 2), mouseY - (y - (padding / 2)), s2, s2, scale);

            if (hovering) {
                component.onClick.run();
                break;
            }

            if (component.end) {
                bottomRow += 1;
            } else {
                topRow += 1;
            }
        }
    }
}
