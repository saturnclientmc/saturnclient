package org.saturnclient.saturnmods.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

public class FpsDisplay implements SaturnMod, HudMod {

    public static ConfigManager config = new ConfigManager("Fps Display");

    public static Property<Boolean> enabled = config.property(
        "Enabled",
        new Property<>(false)
    );

    public static ModDimensions dimensions = new ModDimensions(
        config,
        0,
        0,
        40,
        8
    );

    public static Property<Integer> fgColor = config.property(
        "Foreground color",
        new Property<>(SaturnClient.WHITE, Property.PropertyType.HEX)
    );

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }

    @Override
    public void renderDummy(DrawContext context) {
        int fps = 128;

        context.drawText(
            SaturnClient.textRenderer,
            String.valueOf(fps) + " FPS",
            0,
            0,
            fgColor.value,
            false
        );
    }

    @Override
    public void render(DrawContext context) {
        int fps = MinecraftClient.getInstance().getCurrentFps();

        context.drawText(
            SaturnClient.textRenderer,
            String.valueOf(fps) + " FPS",
            0,
            0,
            fgColor.value,
            false
        );
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
