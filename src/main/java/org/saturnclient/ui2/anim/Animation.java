package org.saturnclient.ui2.anim;

import java.util.function.Consumer;
import java.util.function.Function;

import org.saturnclient.ui2.Element;

public abstract class Animation {
    public int duration;

    Animation(int duration) {
        this.duration = duration;
    }

    public abstract void tick(float progress, Element element);
    public abstract void init(Element element);

    public static void execute(Consumer<Float> tick, int duration) {
        new Thread(() -> {
            Function<Double, Double> curveFunction = Curve::easeInOutCubic;
            float progress = 0.0f;
            double incremental = (1000 - duration) / 10000.0;

            for (int i = 0; i <= 100; i++) {
                float newProgress = (float) (double) curveFunction.apply(i / 100.0);

                while (Math.abs(newProgress - progress) > 1e-6) {
                    if (progress < newProgress) {
                        progress += incremental;
                        if (progress > newProgress) progress = newProgress;
                    } else {
                        progress -= incremental;
                        if (progress < newProgress) progress = newProgress;
                    }

                    tick.accept(newProgress);

                    try {
                        Thread.sleep(duration / 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }            
                }
            }
        }).start();
    }
}
