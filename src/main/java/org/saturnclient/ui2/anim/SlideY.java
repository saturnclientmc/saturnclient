package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public class SlideY extends Animation {
    int offset = 20;
    int maxOffset = 20;
    double startY;

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
    public void init(Element element) {
        startY = element.y - offset;
        element.y = (int) startY;
    }

    @Override
    public void tick(double progress, Element element) {
        double currentY = startY + offset * progress;
        element.y = (int) currentY;
    }
}
