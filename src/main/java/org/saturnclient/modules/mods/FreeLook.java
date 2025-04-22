package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import net.minecraft.client.option.Perspective;

public class FreeLook extends Module {
    public static boolean isFreeLooking = false;
    public static Perspective lastPerspective = null;

    private static ConfigManager config = new ConfigManager("Free Look");

    public static Property<Boolean> toggle = config.property(
            "Toggle Free Look",
            new Property<>(false));

    public FreeLook() {
        super(config, "Free Look", "freelook");
    }
}
