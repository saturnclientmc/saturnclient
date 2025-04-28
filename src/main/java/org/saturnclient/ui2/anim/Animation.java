package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public abstract class Animation {
    public int duration;

    Animation(int duration) {
        this.duration = duration;
    }

    public abstract void tick(float progress, Element element);
    public abstract void init(Element element);
}
