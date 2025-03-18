package org.saturnclient.saturnmods;

import net.minecraft.util.Identifier;

public interface SaturnMod {
    public boolean isEnabled();

    public void setEnabled(boolean e);

    public String getName();

    public Identifier getIconTexture();
}
