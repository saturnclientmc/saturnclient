package org.saturnclient.ui2.anim;

import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.ui2.Element;

public class FadeOut extends Animation {
    float targetOpacity;

    public FadeOut(int duration) {
        super(duration);
    }

    public FadeOut(AnimationConfig config) {
        super(config);
    }

    @Override
    public void tick(double progress, Element element) {
        element.opacity = targetOpacity - (float) progress;
    }

    @Override
    public void init(Element element) {
        targetOpacity = element.opacity;
        // element.opacity = 1.0f;
    }
}
