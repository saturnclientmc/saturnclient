package org.saturnclient.ui2.components;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Theme;
import org.saturnclient.ui2.resources.Fonts;
import org.saturnclient.ui2.resources.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.screens.ConfigEditor;

import net.minecraft.client.render.RenderLayer;

public class SaturnModule extends Element {
    // TODO make these states
    // static {
    // theme.propertyStateDefault("enabled", "bg", 0xFF1d202d);
    // theme.propertyStateDefault("enabled", "icon-bg", 0xFF845eee);
    // theme.propertyStateDefault("hovering", "fg", 0xFF845eee);
    // }

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

        // TODO use this
        if (mod.isEnabled()) {
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, Theme.WIDGET_RADIUS.value, Theme.PRIMARY.value);

        int h = height - (p * 2);

        renderScope.drawRoundedRectangle(p, p, h, h, Theme.WIDGET_RADIUS.value, Theme.PRIMARY.value);

        renderScope.drawTexture(mod.getIconTexture(), p + (p / 2), p + (p / 2), 0, 0, h - p, h - p,
                Theme.PRIMARY_FG.value);

        renderScope.drawText(0.6f, mod.getName(), p + h + 4, p + 1, Theme.FONT.value, Theme.PRIMARY_FG.value);

        renderScope.drawText(0.35f, mod.getVersion(),
                width - p - (int) (Fonts.getWidth(mod.getVersion(), Theme.FONT.value) * 0.35f), p + 1, Theme.FONT.value,
                Theme.PRIMARY_FG.value);

        int xt = p + h + 4;
        for (String tag : mod.getTags()) {
            int xtb = (int) (Fonts.getWidth(tag, Theme.FONT.value) * 0.4f);
            renderScope.drawRoundedRectangle(xt, p + (h / 2), xtb + 6, 12, 5, Theme.ACCENT.value);
            renderScope.drawText(0.4f, tag, xt + 2, p + (h / 2) + 3, Theme.FONT.value, Theme.ACCENT_FG.value);
            xt += xtb + 9;
        }

        // TODO
        if (settingsHover) {
        }

        renderScope.drawTexture(Textures.SETTINGS, width - s - p, height - s - p, 0, 0, s, s, Theme.PRIMARY_FG.value);
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
