package org.saturnclient.ui.animations;

import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnWidget;

public class Slide extends SaturnAnimation {
    private int offset;

    public Slide(int delay, int offset) {
        this.delay = delay;
        this.offset = offset;
    }

    public void init(SaturnWidget widget) {
        widget.y += offset;
    }

    public boolean run(SaturnWidget widget, int tick) {
        widget.y--;
        offset--;

        return offset == 0;
    }
}
