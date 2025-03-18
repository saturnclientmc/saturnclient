package org.saturnclient.saturnmods;

public interface SaturnMod {
    public boolean isEnabled();

    public void setEnabled(boolean e);

    public String getName();
}
