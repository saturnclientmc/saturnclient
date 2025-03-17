package org.saturnclient.saturnmods;

public interface SaturnMod {
    public static boolean enabled = false;

    default public boolean isEnabled() {
        return enabled;
    }

    public String getName();
}
