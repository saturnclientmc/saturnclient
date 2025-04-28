package org.test.test;

import java.util.function.Function;

public class AnimationExample {
    public static void main(String[] args) {
        long stime = System.currentTimeMillis();
        Function<Float, Float> curveFunction = CurveFunctions::easeOut;
        int duration = 300;
        int delayOnProgress = duration / 4;

        // 0.1 -> 0.5 = +=0.01

        float progress = 0f;

        while (System.currentTimeMillis() - stime < duration) {
            float newProgress = Math.min(1.0f, curveFunction.apply(progress + 0.05f));

            if (newProgress == progress) {
                newProgress += 0.05;
            }

            while (progress != newProgress) {
                if (progress > newProgress) {
                    progress -= 0.05;
                    if (progress < newProgress) progress = newProgress; 
                } else if (progress < newProgress) {
                    progress += 0.05;
                    if (progress > newProgress) progress = newProgress; 
                } else {
                    progress += 0.05;
                }
            }

            System.out.println("New Progress: " + progress);

        }


        System.out.println((System.currentTimeMillis() - stime) + "ms");
    }

    // Define the EasingFunction interface (to be passed to functions like easeInOut)
    @FunctionalInterface
    public interface EasingFunction {
        double apply(double t); // Apply the easing function to the normalized progress
    }
}
