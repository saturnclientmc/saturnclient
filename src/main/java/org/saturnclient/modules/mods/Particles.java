package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class Particles extends Module {
    public static ConfigManager config = new ConfigManager("Particles");

    public static ConfigManager totem = new ConfigManager(config, "Totem particles");

    public static Property<Boolean> totem_enabled = totem.property(
            "Change totem color",
            new Property<>(false));

    public static Property<Integer> totem_1 = totem.property(
            "Totem particle color 1",
            new Property<>(SaturnClientConfig.color.value, Property.PropertyType.HEX));

    public static Property<Integer> totem_2 = totem.property(
            "Totem particle color 2",
            new Property<>(SaturnClientConfig.color.value, Property.PropertyType.HEX));

    public Particles() {
        super(config, "Particles", "particles");
    }
}
