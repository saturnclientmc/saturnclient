package org.saturnclient.ui2.anim;

public class Curve {
    public static double easeInOutCubic(double x) {
        return (x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    public static double easeInOutCubicReverse(double x) {
        return -(x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2);
    }
}
