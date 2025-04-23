package org.saturnclient.modules.mods;

import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.config.NamedProperty;
import org.saturnclient.saturnclient.config.Property;

public class Crosshair extends Module {
    public static NamedProperty<Boolean> enabled = new Property<>(false).named("Enabled");
    public static NamedProperty<Boolean> range_indicator = new Property<>(false).named("Range Indicator");

    public Crosshair() {
        super("Crosshair", "crosshair", enabled, range_indicator);
    }

    static {
        ModManager.impl(Crosshair.class);
    }

    @Override
    public boolean isEnabled() {
        return enabled.prop.value;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.prop.value = e;
    }
}
