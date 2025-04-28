package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public class SlideY extends Animation {
    int offset = 20;
    int maxOffset = 20;
    int elementY;

    public SlideY(int duration) {
        super(duration);
    }

    public SlideY(int duration, int offset) {
        super(duration);

        this.offset = offset;
        this.maxOffset = offset;
    }

    public SlideY(int duration, int offset, int maxOffset) {
        super(duration);

        this.offset = offset;
        this.maxOffset = offset;
    }
    
    @Override
    public void tick(float progress, Element element) {
        if (progress > 0) {
            element.y = elementY + Math.min(maxOffset, (int) (offset * progress));
        } else {
            element.y = elementY + Math.max(-offset, (int) (offset * progress));
        }
    }

    @Override
    public void init(Element element) {
        element.y += offset;
        elementY = element.y;
    }
}
