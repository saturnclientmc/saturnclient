package org.saturnclient.ui2.anim;

import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.ui2.Element;

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
