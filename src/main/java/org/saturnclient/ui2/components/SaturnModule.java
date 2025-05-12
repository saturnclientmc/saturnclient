package org.saturnclient.ui2.components;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.screens.ConfigEditor;

import net.minecraft.client.render.RenderLayer;

public class SaturnModule extends Element {
    private static ThemeManager theme = new ThemeManager("Module", "enabled", "hovering");
    private static Property<Integer> bgColor = theme.property("bg", new Property<>(0xFF581c1c));
    private static Property<Integer> fgColor = theme.property("fg", new Property<>(0xFFA1A2B8));
    private static Property<Integer> iconBg = theme.property("icon-bg", new Property<>(0xFF802222));
    private static Property<Integer> iconRadius = theme.property("icon-radius", new Property<>(10));
    private static Property<Integer> radius = theme.property("radius", new Property<>(10));

    static {
        theme.propertyStateDefault("enabled", "bg", 0xFF14532d);
        theme.propertyStateDefault("enabled", "icon-bg", 0xFF1e6f3f);
        theme.propertyStateDefault("hovering", "fg", 0xFFbfc0cf);
    }

    private Module mod;

    public SaturnModule(Module mod) {
        this.mod = mod;
        this.width = 160;
        this.height = 50;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        int p = 10;
        int s = 14;
        boolean settingsHover = false;

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        if (mod.isEnabled()) {
            theme.setState("enabled");
        }

        if (ctx.isHovering(width - s - p - 5, height - s - p - 5, s + 10, s + 10)) {
            settingsHover = true;
        } else if (ctx.isHovering()) {
            theme.applyState("hovering");
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, radius.value, bgColor.value);

        int h = height-(p * 2);

        renderScope.drawRoundedRectangle(p, p, h, h, iconRadius.value, iconBg.value);

        renderScope.drawTexture(mod.getIconTexture(), p + (p / 2), p + (p / 2), 0, 0, h - p, h - p, fgColor.value);

        renderScope.drawText(0.6f, mod.getName(), p+h+4, p + 1, true, fgColor.value);

        if (settingsHover) {
            theme.setState("hovering");
        }

        renderScope.drawTexture(Textures.SETTINGS, width - s - p, height - s - p, 0, 0, s, s, fgColor.value);

        theme.setState(null);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        int p = 10;
        int s = 14;

        if (Utils.isHovering(mouseX - (width - s - p - 5), mouseY - (height - s - p - 5), s + 10, s + 10, 1.0f)) {
            SaturnClient.client.setScreen(new ConfigEditor(mod.getConfig()));
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }
}
