package org.saturnclient.modules.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.SaturnMod;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.Textures;

public class FpsDisplay implements SaturnMod, HudMod {

    public static ConfigManager config = new ConfigManager("Fps Display");

    public static Property<Boolean> enabled = config.property(
            "Enabled",
            new Property<>(false));

    public static ModDimensions dimensions = new ModDimensions(
            config,
            0,
            0,
            40,
            8);

    public static Property<Integer> fgColor = config.property(
            "Foreground color",
            new Property<>(SaturnClientConfig.WHITE, Property.PropertyType.HEX));

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }

    @Override
    public void renderDummy(DrawContext context) {
        int fps = 128;

        context.drawText(
                SaturnClientConfig.textRenderer,
                String.valueOf(fps) + " FPS",
                0,
                0,
                fgColor.value,
                false);
    }

    @Override
    public void render(DrawContext context) {
        int fps = SaturnClient.client.getCurrentFps();

        context.drawText(
                SaturnClientConfig.textRenderer,
                String.valueOf(fps) + " FPS",
                0,
                0,
                fgColor.value,
                false);
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public String getName() {
        return "Fps Display";
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("fps");
    }

    @Override
    public ConfigManager getConfig() {
        return config;
    }
}
