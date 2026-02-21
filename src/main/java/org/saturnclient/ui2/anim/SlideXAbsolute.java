package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public class SlideXAbsolute extends Animation {
    int offset = 20;
    int maxOffset = 20;
    int elementX;

    public SlideXAbsolute(int duration) {
        super(duration);
    }

    public SlideXAbsolute(int duration, int offset) {
        super(duration);
        this.offset = offset;
        this.maxOffset = offset;
    }

    public SlideXAbsolute(int duration, int offset, int maxOffset) {
        super(duration);
        this.offset = offset;
        this.maxOffset = maxOffset;
    }

    @Override
    public void tick(double progress, Element element) {
        element.x = elementX + (int) (offset * progress);
    }

    @Override
    public void init(Element element) {
        elementX = element.x;
    }
}
