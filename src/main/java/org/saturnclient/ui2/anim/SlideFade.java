package org.saturnclient.ui2.anim;

import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.ui2.Element;

public class SlideFade extends Animation {
    public Fade fade;
    public SlideY slideY;

    public SlideFade(int duration, int offset) {
        super(duration);
        this.fade = new Fade(duration);
        this.slideY = new SlideY(duration, offset);
    }

    public SlideFade(AnimationConfig config, int offset) {
        super(config);
        this.fade = new Fade(duration);
        this.slideY = new SlideY(duration, offset);
    }

    @Override
    public void tick(double progress, Element element) {
        fade.delay = this.delay;
        fade.duration = this.duration;

        slideY.delay = this.delay;
        slideY.duration = this.duration;

        fade.tick(progress, element);
        slideY.tick(progress, element);
    }

    @Override
    public void init(Element element) {
        fade.init(element);
        slideY.init(element);
    }
}
