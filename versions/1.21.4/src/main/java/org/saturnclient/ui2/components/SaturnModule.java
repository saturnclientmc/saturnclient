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

    private final Module mod;

    static int p = 10; // padding
    static int s = 14; // settings icon size
    static int expand = 5; // settings icon click area

    public SaturnModule(Module mod) {
        this.mod = mod;
        this.width = 160;
        this.height = 50;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        boolean hovered = ctx.isHovering();
        boolean enabled = mod.isEnabled();

        int iconX = width - s - p;
        int iconY = height - s - p;

        boolean settingsHover = ctx.isHovering(
                getSettingsX(),
                getSettingsY(),
                getSettingsHitBox(),
                getSettingsHitBox());

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        // Resolve theme colors
        int bgColor = Theme.PRIMARY.value;
        int fgColor = hovered && !settingsHover ? Theme.ACCENT.value : Theme.PRIMARY_FG.value;

        int iconBg = enabled ? Theme.ACCENT.value : Theme.PRIMARY.value;
        int iconFg = enabled ? Theme.ACCENT_FG.value : fgColor;

        int settingsColor = settingsHover
                ? Theme.ACCENT.value
                : Theme.withAlpha(160, fgColor);

        int h = height - (p * 2);

        // Main background
        renderScope.drawRoundedRectangle(
                0, 0,
                width, height,
                Theme.WIDGET_RADIUS.value,
                bgColor);

        // Icon background
        renderScope.drawRoundedRectangle(
                p, p,
                h, h,
                Theme.WIDGET_RADIUS.value,
                iconBg);

        // Icon
        renderScope.drawTexture(
                mod.getIconTexture(),
                p + (p / 2),
                p + (p / 2),
                0, 0,
                h - p,
                h - p,
                iconFg);

        // Module name
        renderScope.drawText(
                0.6f,
                mod.getName(),
                p + h + 4,
                p + 1,
                Theme.FONT.value,
                fgColor);

        // Version (right aligned)
        String version = mod.getVersion();
        int versionWidth = (int) (Fonts.getWidth(version, Theme.FONT.value) * 0.35f);

        renderScope.drawText(
                0.35f,
                version,
                width - p - versionWidth,
                p + 1,
                Theme.FONT.value,
                fgColor);

        // Tags
        int xt = p + h + 4;
        for (String tag : mod.getTags()) {
            int tagWidth = (int) (Fonts.getWidth(tag, Theme.FONT.value) * 0.4f);

            renderScope.drawRoundedRectangle(
                    xt,
                    p + (h / 2),
                    tagWidth + 6,
                    12,
                    5,
                    Theme.ACCENT.value);

            renderScope.drawText(
                    0.4f,
                    tag,
                    xt + 2,
                    p + (h / 2) + 3,
                    Theme.FONT.value,
                    Theme.ACCENT_FG.value);

            xt += tagWidth + 9;
        }

        // Settings icon
        renderScope.drawTexture(
                Textures.SETTINGS,
                iconX,
                iconY,
                0, 0,
                s, s,
                settingsColor);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        boolean settingsHover = Utils.isHovering(
                mouseX - getSettingsX(),
                mouseY - getSettingsY(),
                getSettingsHitBox(),
                getSettingsHitBox(),
                1.0f);

        if (settingsHover) {
            SaturnClient.client.setScreen(new ConfigEditor(mod.getConfig()));
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }

    private int getSettingsX() {
        return width - s - p - expand;
    }

    private int getSettingsY() {
        return height - s - p - expand;
    }

    private int getSettingsHitBox() {
        return s + expand * 2;
    }
}