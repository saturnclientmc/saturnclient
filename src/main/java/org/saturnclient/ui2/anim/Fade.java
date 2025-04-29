package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public class Fade extends Animation {
    public Fade(int duration) {
        super(duration);
    }

    @Override
    public void tick(float progress, Element element) {
        element.opacity = progress;
        // element.opacity += 0.1f;
    }

    @Override
    public void init(Element element) {
        element.opacity = 0;
    }
}
