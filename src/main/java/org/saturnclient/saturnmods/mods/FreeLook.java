package org.saturnclient.saturnmods.mods;

import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

import net.minecraft.client.option.Perspective;
import net.minecraft.util.Identifier;

public class FreeLook implements SaturnMod {
    public static boolean isFreeLooking = false;
    public static Perspective lastPerspective = null;

    private static ConfigManager config = new ConfigManager("Free Look");
    public static Property<Boolean> enabled = config.property(
            "Enabled",
            new Property<>(false));

    public static Property<Boolean> toggle = config.property(
            "Toggle Free Look",
            new Property<>(false));

    public boolean isEnabled() {
        return enabled.value;
    }

    public void setEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public ConfigManager getConfig() {
        return config;
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("freelook");
    }

    @Override
    public String getName() {
        return "Free Look";
    }
}
