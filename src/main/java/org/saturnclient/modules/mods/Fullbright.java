package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.config.Property;

public class Fullbright extends Module {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Integer> brightness = Property.integer(100);

    public Fullbright() {
        super(
            new ModuleDetails("Fullbright", "fullbright")
            .description("Allows you to see in the dark")
            .tags("Camera")
            .version("v0.1.0"),
            enabled.named("Enabled"),
            brightness.named("Brightness %"));
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
    
    public static boolean shouldOverrideBrightness() {
        return enabled.value;
    }
    
    public static float getBrightnessValue() {
        return brightness.value / 10;
    }

}
