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
    private static ThemeManager theme = new ThemeManager("SaturnModule", "hovering", "enabled", "settings-hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(-16777216));
    private static Property<Integer> nameColor = theme.property("name-color", new Property<Integer>(-1));
    private static Property<Integer> descriptionColor = theme.property("description-color", new Property<Integer>(-5592406));
    private static Property<Integer> barColor = theme.property("bar-color", new Property<Integer>(-1));
    private static Property<Integer> settingsFg = theme.property("settings-fg", new Property<Integer>(-1));
    private static Property<Integer> settingsBg = theme.property("settings-bg", new Property<Integer>(-16777216));
    private static Property<Integer> iconFg = theme.property("icon-fg", new Property<Integer>(-1));

    private static Property<Integer> cornerRadius = theme.property("corner-radius", new Property<Integer>(10));
    private static Property<Integer> barWidth = theme.property("bar-width", new Property<Integer>(7));
    private static Property<Integer> padding = theme.property("padding", new Property<Integer>(10));

    int settingsHeight = 26;

    static {
        theme.propertyStateDefault("enabled", "bar-color", -7643914);
        theme.propertyStateDefault("hovering", "name-color", -7643914);
        theme.propertyStateDefault("settings-hovering", "name-color", -7643914);
        theme.propertyStateDefault("settings-hovering", "settings-bg", -7643914);
    }

    private Module mod;

    public SaturnModule(Module mod) {
        this.mod = mod;
        this.width = 270;
        this.height = 120;
    }

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        if (mod.isEnabled()) {
            theme.setState("enabled");
        } else {
            theme.setState(null);
        }

        if (ctx.isHovering(barWidth.value + padding.value, height - padding.value - settingsHeight, width - barWidth.value - (padding.value * 2), settingsHeight)) {
            theme.applyState("settings-hovering");
        } else if (ctx.isHovering()) {
            theme.applyState("hovering");
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);

        renderScope.enableScissor(0, 0, barWidth.value, height);
        renderScope.drawRoundedRectangle(0, 0, barWidth.value * 2, height, cornerRadius.value, barColor.value);
        renderScope.disableScissor();

        renderScope.drawTexture(mod.getIconTexture(), barWidth.value + padding.value, padding.value, 0, 0, 21, 21, iconFg.value);
        renderScope.drawText(mod.getName(), (int) (padding.value * 1.5f) + 21 + barWidth.value, padding.value + ((23 - Fonts.getHeight()) / 2), true, nameColor.value);

        renderScope.matrices.push();
        renderScope.matrices.translate(padding.value + barWidth.value, 42, 0);
        renderScope.matrices.scale(0.7f, 0.7f, 1.0f);
        renderScope.drawText(formatText(mod.getDescription(), width - barWidth.value - padding.value), 0, 0, false, descriptionColor.value);
        renderScope.matrices.pop();

        renderScope.drawRoundedRectangle(barWidth.value + padding.value, height - padding.value - settingsHeight, width - (padding.value * 2) - barWidth.value, settingsHeight, 10, settingsBg.value);

        renderScope.drawTexture(Textures.SETTINGS, barWidth.value + (padding.value * 2), height - padding.value - settingsHeight + 5, 0, 0, 16, 16);

        renderScope.matrices.push();
        renderScope.matrices.translate(barWidth.value + (padding.value * 2) + 20, height - padding.value - (settingsHeight / 2), 0);
        renderScope.matrices.scale(0.7f, 0.7f, 1);
        renderScope.matrices.translate(0, -(Fonts.getHeight() / 2) + 2, 0);
        renderScope.drawText("Settings", 0, 0, false, settingsFg.value);
        renderScope.matrices.pop();
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (Utils.isHovering(mouseX - (barWidth.value + padding.value), mouseY - (height - padding.value - settingsHeight), width - barWidth.value - (padding.value * 2), settingsHeight, scale)) {
            SaturnClient.client.setScreen(new ConfigEditor(mod.getConfig()));
        } else {
            mod.setEnabled(!mod.isEnabled());
        }
    }

    private static String formatText(String text, int width) {
        width -= Fonts.getWidth("..", Fonts.INTER);

        StringBuilder trimmedText = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            String current = trimmedText.toString() + text.charAt(i);
            double currentWidth = Fonts.getWidth(current, Fonts.INTER) * 0.7;
            
            if (currentWidth > width) {
                trimmedText.append("..");
                break;
            }
            
            trimmedText.append(text.charAt(i));
        }
        
        return trimmedText.toString();
    }    
}
