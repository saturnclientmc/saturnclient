package org.saturnclient.ui2.components;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.resources.Fonts;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.screens.ConfigEditor;

import net.minecraft.client.render.RenderLayer;

public class SaturnModule extends Element {
    private static ThemeManager theme = new ThemeManager("Module", "enabled", "hovering");
    private static Property<Integer> bgColor = theme.property("bg", Property.color(0xFF1d202d));
    private static Property<Integer> fgColor = theme.property("fg", Property.color(0xFFA1A2B8));
    private static Property<Integer> iconFg = theme.property("icon-fg", Property.color(0xFFA1A2B8));
    private static Property<Integer> iconBg = theme.property("icon-bg", Property.color(0xFF2e3248));
    private static Property<Integer> tagBg = theme.property("tag-bg", Property.color(0xFF2e3248));
    private static Property<Integer> iconRadius = theme.property("icon-radius", Property.color(10));
    private static Property<Integer> font = theme.property("font", Property.font(1));
    private static Property<Integer> radius = theme.property("radius", Property.color(10));

    static {
        theme.propertyStateDefault("enabled", "bg", 0xFF1d202d);
        theme.propertyStateDefault("enabled", "icon-bg", 0xFF845eee);
        theme.propertyStateDefault("hovering", "fg", 0xFF845eee);
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

        renderScope.drawTexture(mod.getIconTexture(), p + (p / 2), p + (p / 2), 0, 0, h - p, h - p, iconFg.value);

        renderScope.drawText(0.6f, mod.getName(), p+h+4, p + 1, font.value, fgColor.value);

        renderScope.drawText(0.35f, mod.getVersion(), width - p - (int) (Fonts.getWidth(mod.getVersion(), font.value) * 0.35f), p + 1, font.value, fgColor.value);

        int xt = p + h + 4;
        for (String tag : mod.getTags()) {
            int xtb = (int) (Fonts.getWidth(tag, font.value) * 0.4f);
            renderScope.drawRoundedRectangle(xt, p + (h/2), xtb + 6, 12, 5, tagBg.value);
            renderScope.drawText(0.4f, tag, xt+2, p + (h/2) + 3, font.value, fgColor.value);
            xt += xtb + 9;
        }

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
