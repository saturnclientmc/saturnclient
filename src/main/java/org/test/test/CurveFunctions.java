package org.test.test;

public class CurveFunctions {
    public static float linear(float t) {
        return t;
    }

    public static float easeIn(float t) {
        return t * t;
    }

    public static float easeOut(float t) {
        return (float)(1 - Math.pow(1 - t, 2));
    }

    public static float easeInOut(float t) {
        return (float)((-Math.cos(Math.PI * t) / 2.0) + 0.5);
    }

    public static float bounceOut(float t) {
        if (t < (1/2.75f)) {
            return 7.5625f * t * t;
        } else if (t < (2/2.75f)) {
            t -= (1.5f/2.75f);
            return 7.5625f * t * t + 0.75f;
        } else if (t < (2.5/2.75)) {
            t -= (2.25f/2.75f);
            return 7.5625f * t * t + 0.9375f;
        } else {
            t -= (2.625f/2.75f);
            return 7.5625f * t * t + 0.984375f;
        }
    }
}
