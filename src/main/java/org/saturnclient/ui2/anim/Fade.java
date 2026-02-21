package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public class Fade extends Animation {
    float targetOpacity;

    public Fade(int duration) {
        super(duration);
    }

    @Override
    public void tick(double progress, Element element) {
        element.opacity = targetOpacity * (float) progress;
    }

    @Override
    public void init(Element element) {
        targetOpacity = element.opacity;
        element.opacity = 0;
    }
}
