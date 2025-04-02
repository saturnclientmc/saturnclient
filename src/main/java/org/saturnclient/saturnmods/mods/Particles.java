package org.saturnclient.saturnmods.mods;

import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

public class Particles implements SaturnMod {
    public static ConfigManager config = new ConfigManager("Particles");

    public static Property<Boolean> enabled = config.property(
            "Enabled",
            new Property<>(false));

    public static Property<Boolean> totem_enabled = config.property(
            "Change totem color",
            new Property<>(false));

    public static Property<Integer> totem_1 = config.property(
            "Totem particle color 1",
            new Property<>(SaturnClient.COLOR.value, Property.PropertyType.HEX));

    public static Property<Integer> totem_2 = config.property(
            "Totem particle color 2",
            new Property<>(SaturnClient.COLOR.value, Property.PropertyType.HEX));

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
        return "Particles";
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("particles");
    }

    @Override
    public ConfigManager getConfig() {
        return config;
    }
}
