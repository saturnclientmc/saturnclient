package org.saturnclient.modules.mods;

import net.minecraft.util.Identifier;

import org.saturnclient.modules.SaturnMod;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui.Textures;

public class Crosshair implements SaturnMod {
    public static ConfigManager config = new ConfigManager("Crosshair");

    public static Property<Boolean> enabled = config.property(
            "Enabled",
            new Property<>(false));

    public static Property<Boolean> range_indicator = config.property(
            "Hit range indicator",
            new Property<>(false));

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
        return "Crosshair";
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("crosshair");
    }

    @Override
    public ConfigManager getConfig() {
        return config;
    }
}
