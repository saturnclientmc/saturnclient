package org.saturnclient.ui2.anim;

import java.time.Instant;
import java.util.function.Function;

import org.saturnclient.saturnclient.SaturnClient;

public class Curve {
    // Define the curve functions (ease-in, ease-out, ease-in-out)
    public static int easeIn(float progress, long elapsedTime, float totalDuration) {
        // Ease-in: Modify the curve based on elapsed time
        float t = progress;
        t *= t;  // Ease-in: t^2
        // Scale the delay based on the remaining time
        return (int) ((t * (totalDuration - elapsedTime)) / totalDuration);
    }

    public static int easeOut(float progress, long elapsedTime, float totalDuration) {
        // Ease-out: Modify the curve based on elapsed time
        float t = progress;
        t = 1 - (1 - t) * (1 - t);  // Ease-out: 1 - (1 - t)^2
        // Scale the delay based on the remaining time
        return (int) ((t * (totalDuration - elapsedTime)) / totalDuration);
    }

    public static int easeInOut(float progress, long elapsedTime, float totalDuration) {
        // Ease-in-out: Modify the curve based on elapsed time
        double t = progress;
        if (t < 0.5) {
            t = 2 * t * t;  // Ease-in: t^2
        } else {
            t = 1 - Math.pow(-2 * t + 2, 2) / 2;  // Ease-out: 1 - (2t-2)^2 / 2
        }
        // Scale the delay based on the remaining time
        return (int) ((t * (totalDuration - elapsedTime)) / totalDuration);
    }

    public static void animate(float totalDuration, float increment) {
        long startTime = System.currentTimeMillis();
        float progress = 0.0f;
        float progressPerStep = increment;
        long elapsedTime = 0;  // Track the total elapsed time

        // Animation loop until progress reaches 1.0
        while (progress < 1.0f) {
            // Calculate the current progress value
            progress = Math.min(progress + progressPerStep, 1.0f);  // Ensure progress does not exceed 1.0

            // Calculate the elapsed time
            elapsedTime = System.currentTimeMillis() - startTime;

            // Calculate the delay based on the current progress and elapsed time using the easing function
            // We switch between easeIn, easeOut, and easeInOut for different effects
            int delay = easeInOut(progress, elapsedTime, totalDuration);

            // Print the progress and delay for debugging
            System.out.println("Progress: " + (progress * 100) + "%, Delay: " + delay + "ms");

            // Ensure we don't sleep for a negative value
            if (delay < 1) {
                delay = 1;  // Avoid immediate jumps, ensure some delay for visual effect
            }

            // Sleep for the delay calculated by the easing function
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the elapsed time exceeds the total duration, break out of the loop
            if (elapsedTime >= totalDuration) {
                break;
            }
        }

        System.out.println("Animation Complete!");
    }

    public static void main(String[] args) {
        float durationInMillis = 500.0f;  // Total duration of the animation in ms
        float increment = 0.01f;  // Increment the progress by 1% each step
        animate(durationInMillis, increment);  // Start the animation
    }
}
