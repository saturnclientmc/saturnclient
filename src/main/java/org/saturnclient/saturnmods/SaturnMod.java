package org.saturnclient.saturnmods;

import org.saturnclient.saturnclient.config.ConfigManager;

import net.minecraft.util.Identifier;

public interface SaturnMod {
    public boolean isEnabled();

    public void setEnabled(boolean e);

    public String getName();

    public Identifier getIconTexture();

    public ConfigManager getConfig();
}
