package org.saturnclient.ui2.anim;

import java.util.function.Function;

import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.ui2.Element;

public abstract class Animation {
    public int duration;
    public int delay = 0;
    public Function<Double, Double> curve = Curve::easeInOutCubic;

    Animation(AnimationConfig config) {
        this.duration = config.duration.value;
        switch (config.curve.value) {
            case 0:
                this.curve = Curve::easeInOutCubic;
                break;
            case 1:
                this.curve = Curve::easeOutCubic;
                break;
            case 2:
                this.curve = Curve::easeInOutBack;
                break;
            default:
                break;
        }
    }

    Animation(int duration) {
        this.duration = duration;
    }

    public abstract void tick(double progress, Element element);

    public abstract void init(Element element);
}
