package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class Crosshair extends Module {
    public static ConfigManager config = new ConfigManager("Crosshair");

    public static Property<Boolean> range_indicator = config.property(
            "Hit range indicator",
            new Property<>(false));

    public Crosshair() {
        super(config, "Crosshair", "crosshair");
    }
}
