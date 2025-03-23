package org.saturnclient.ui;

public class SaturnAnimation {

    public int delay = 0;
    public int lastTick = 0;

    public void init(SaturnWidget widget) {}

    public boolean run(SaturnWidget widget, int tick) {
        return true;
    }
}
