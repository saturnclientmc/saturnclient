package org.saturnclient.ui2.components;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.menus.ConfigEditor;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.render.RenderLayer;

public class SaturnModule extends Element {
    private static ThemeManager theme = new ThemeManager("SaturnModule", "enabled", "hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(-16777216));
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(-1));
    private static Property<Integer> iconBgColor = theme.property("icon-bg-color", new Property<Integer>(-16777216));
    private static Property<Integer> iconColor = theme.property("icon-fg-color", new Property<Integer>(-1));

    private static Property<Integer> cornerRadius = theme.property("corner-radius", new Property<Integer>(10));
    private static Property<Integer> padding = theme.property("padding", new Property<Integer>(10));
    private static Property<Integer> iconMargin = theme.property("icon-margin", new Property<Integer>(10));
    private static Property<Integer> iconPadding = theme.property("icon-padding", new Property<Integer>(4));

    private static ThemeManager settingsTheme = new ThemeManager("SaturnModule$Settings", "hovering");
    private static Property<Integer> settingsBg = settingsTheme.property("bg-color", new Property<Integer>(-16777216));
    private static Property<Integer> settingsFg = settingsTheme.property("fg-color", new Property<Integer>(-1));
    private static Property<Integer> settingsRadius = settingsTheme.property("corner-radius", new Property<Integer>(10));

    static {
        theme.propertyStateDefault("enabled", "icon-bg-color", -7643914);
        theme.propertyStateDefault("hovering", "fg-color", -7643914);
        settingsTheme.propertyStateDefault("hovering", "bg-color", -7643914);
    }

    private Module mod;

    public SaturnModule(Module mod) {
        this.mod = mod;
        this.width = 180;
        this.height = 140;
    }

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        if (mod.isEnabled()) {
            theme.setState("enabled");
        }

        if (ctx.isHovering(0, (height-40), width, 30)) {
            settingsTheme.setState("hovering");
        } else if (ctx.isHovering()) {
            theme.applyState("hovering");
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        renderScope.matrices.push();

        renderScope.matrices.translate(padding.value, padding.value, 0);

        renderScope.drawRoundedRectangle(0, 0, 20, 20, 10, iconBgColor.value);
        renderScope.drawTexture(mod.getIconTexture(), iconPadding.value, iconPadding.value, 0, 0, 20 - (iconPadding.value * 2), 20 - (iconPadding.value * 2), iconColor.value);
        renderScope.drawText(mod.getName(), 20 + iconMargin.value, (22 - Fonts.getHeight()) / 2, false, fgColor.value);
        renderScope.matrices.push();
        renderScope.matrices.translate(20 + iconMargin.value, ((22 - Fonts.getHeight()) / 2) + Fonts.getHeight(), 0);

        renderScope.matrices.scale(0.5f, 0.5f, 1);
        renderScope.drawText(formatText(mod.getDescription(), (int) ((width - iconMargin.value) * 1.5f)), 0, 0, false, -20);
        renderScope.matrices.pop();

        renderScope.matrices.pop();

        // Settings button
        renderScope.matrices.push();
        renderScope.matrices.translate(padding.value, height - 30 - padding.value, 0);

        int w = width - (padding.value * 2);

        renderScope.drawRoundedRectangle(0, 0, w, 30, settingsRadius.value, settingsBg.value);
        renderScope.drawTexture(Textures.SETTINGS, ((w - Fonts.getWidth("Settings", false)) / 2) - 20, (16) / 2, 0, 0, 15, 15, settingsFg.value);
        renderScope.drawText("Settings", Fonts.centerX(w, "Settings", false), Fonts.centerY(32), false, settingsFg.value);
        renderScope.matrices.pop();

        theme.setState(null);
        settingsTheme.setState(null);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (Utils.isHovering(mouseX, mouseY - (height-40), width, 30, scale)) {
            SaturnClient.client.setScreen(new ConfigEditor(mod.getConfig()));
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }

    public static String formatText(String text, int width) {
        StringBuilder formatted = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();
    
        for (char c : text.toCharArray()) {
            currentLine.append(c);
            if (Fonts.getWidth(currentLine.toString(), false) > width) {
                // Line is too wide; backtrack the last character
                currentLine.deleteCharAt(currentLine.length() - 1);
                formatted.append(currentLine).append('\n');
                currentLine.setLength(0); // clear currentLine
                currentLine.append(c); // start new line with the character that overflowed
            }
        }
    
        // Append any remaining characters in the currentLine
        if (currentLine.length() > 0) {
            formatted.append(currentLine);
        }

        return formatted.toString();
    }    
}
