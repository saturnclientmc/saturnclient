package org.saturnclient.ui.anim;

import org.saturnclient.config.AnimationConfig;
import org.saturnclient.ui.Element;

public class SlideX extends Animation {
    int offset = 20;
    double startX;

    public SlideX(AnimationConfig config) {
        super(config);
    }

    public SlideX(AnimationConfig config, int offset) {
        super(config);
        this.offset = offset;
    }

    public SlideX(int duration) {
        super(duration);
    }

    public SlideX(int duration, int offset) {
        super(duration);
        this.offset = offset;
    }

    @Override
    public void init(Element element) {
        startX = element.x - offset;
        element.x = (int) startX;
    }

    @Override
    public void tick(double progress, Element element) {
        double currentX = startX + offset * progress;
        element.x = (int) currentX;
    }
}
