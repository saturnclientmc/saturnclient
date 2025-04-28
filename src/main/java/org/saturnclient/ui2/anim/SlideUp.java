package org.saturnclient.ui2.anim;

import org.saturnclient.ui2.Element;

public class SlideUp extends Animation {
    int targetY;
    
    @Override
    public float tick(int tick, Element element) {
        if (tick == 0) {
            targetY = element.y;
            element.y += 20;
        } else if (element.y > targetY) {
            element.y--;
            return ((float) element.y) / targetY;
        }

        return 0.1f;
    }
}
