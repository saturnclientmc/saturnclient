package org.saturnclient.ui.animations;

import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnWidget;

public class FadeIn extends SaturnAnimation {
    public FadeIn(int delay) {
        this.delay = delay;
    }

    public void init(SaturnWidget widget) {
        widget.alpha = 0.0f;
    }

    public boolean run(SaturnWidget widget, int tick) {
        widget.alpha += 0.05f;

        return widget.alpha > 0.9f;
    }
}
