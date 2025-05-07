package org.saturnclient.ui2.components;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.menus.ConfigEditor;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.render.RenderLayer;

public class SaturnModule extends Element {
    private static ThemeManager theme = new ThemeManager("SaturnModule", "enabled", "hovering", "toggle-hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(-16777216));
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(-1));
    private static Property<Integer> iconColor = theme.property("icon-color", new Property<Integer>(-1));
    private static Property<Integer> toggleFg = theme.property("toggle-fg", new Property<Integer>(-1));
    private static Property<Integer> toggleBg = theme.property("toggle-bg", new Property<Integer>(-16777216));

    private static Property<Integer> cornerRadius = theme.property("corner-radius", new Property<Integer>(25));
    private static Property<Boolean> toggleBold = theme.property("toggle-bold", new Property<Boolean>(false));
    private static Property<Boolean> nameBold = theme.property("name-bold", new Property<Boolean>(false));

    static {
        theme.propertyStateDefault("enabled", "toggle-bg", -7643914);
        theme.propertyStateDefault("hovering", "fg-color", -7643914);
        theme.propertyStateDefault("hovering", "icon-color", -7643914);
    }

    private Module mod;

    public SaturnModule(Module mod) {
        this.mod = mod;
        this.width = 140;
        this.height = 140;
    }

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        if (mod.isEnabled()) {
            theme.setState("enabled");
        } else {
            theme.setState(null);
        }

        if (ctx.isHovering(0, (height-30), width, 30)) {
            theme.applyState("toggle-hovering");
        } else if (ctx.isHovering()) {
            theme.applyState("hovering");
        }

        renderScope.enableScissor(0, 0, width + 10, height - 30);
        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);
        renderScope.disableScissor();

        renderScope.drawTexture(mod.getIconTexture(), (width - 20) / 2, (height - 65) / 2, 0, 0, 20, 20, iconColor.value);

        renderScope.drawText(mod.getName(), Fonts.centerX(width, mod.getName(), focused), Fonts.centerY(height), nameBold.value, fgColor.value);

        renderScope.enableScissor(0, height - 30, width + 10, height);
        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, toggleBg.value);
        renderScope.disableScissor();

        String t = mod.isEnabled() ? "Enabled" : "Disabled";

        renderScope.drawText(t, Fonts.centerX(width, t, toggleBold.value), height - 30 + Fonts.centerY(30), toggleBold.value, toggleFg.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (Utils.isHovering(mouseX, mouseY - (height-30), width, 30, scale)) {
            mod.setEnabled(!mod.isEnabled());
        } else {
            SaturnClient.client.setScreen(new ConfigEditor(mod.getConfig()));
        }
    }
}
