package org.saturnclient.ui2.anim;

public class Curve {
    public static double easeOutCubic(double x) {
        return 1 - Math.pow(1 - x, 3);
    }

    public static double easeInOutCubic(double x) {
        return (x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    public static double easeInOutBack(double x) {
        double c1 = 2.5;
        double c2 = c1 * 1.525;

        if (x < 0.5) {
            return (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2;
        } else {
            return (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (2 * x - 2) + c2) + 2) / 2;
        }
    }
}
