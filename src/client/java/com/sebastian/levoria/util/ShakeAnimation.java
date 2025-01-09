package com.sebastian.levoria.util;

import net.minecraft.client.util.math.MatrixStack;

public class ShakeAnimation {
    private long startTime = 0; // Startzeit der Animation in Nanosekunden
    private boolean animating = false;
    private static final float ANIMATION_DURATION = 0.82f; // Dauer in Sekunden

    public void startAnimation() {
        animating = true;
        startTime = System.nanoTime(); // Startzeit festlegen
    }

    public void updateAnimation() {
        if (animating) {
            // Fortschritt berechnen
            float elapsedTime = (System.nanoTime() - startTime) / 1_000_000_000.0f; // In Sekunden
            if (elapsedTime >= ANIMATION_DURATION) {
                animating = false; // Animation beenden
            }
        }
    }

    public void translateMatrix(MatrixStack matrices) {
        if (animating) {
            // Fortschritt in Prozent berechnen
            float progress = (System.nanoTime() - startTime) / 1_000_000_000.0f / ANIMATION_DURATION;
            float shakeValue = calculateShake(progress);
            matrices.translate(shakeValue, 0, 0); // Nur X-Koordinate verschieben
        }
    }

    private float calculateShake(float progress) {
        if (progress >= 1.0f) {
            return 0.0f; // Animation beendet
        }

        // Apply the cubic Bézier curve
        float bezierProgress = cubicBezier(progress, 0.36f, 0.07f, 0.19f, 0.97f);

        // Interpolate shake values based on the eased progress
        return interpolateShake(bezierProgress);
    }

    private float cubicBezier(float t, float p1x, float p1y, float p2x, float p2y) {
        // Solve Bézier curve using De Casteljau's algorithm
        float u = 1 - t;

        // Calculate X and Y separately
        float x = 3 * u * u * t * p1x + 3 * u * t * t * p2x + t * t * t;
        float y = 3 * u * u * t * p1y + 3 * u * t * t * p2y + t * t * t;

        return y; // Return the Y value for easing
    }

    private float interpolateShake(float easedProgress) {
        // Define keyframes for shake values
        if (easedProgress < 0.1f || easedProgress >= 0.9f) {
            return lerp(-1.0f, 0.0f, easedProgress * 10.0f % 1.0f);
        } else if (easedProgress >= 0.1f && easedProgress < 0.2f) {
            return lerp(0.0f, 2.0f, (easedProgress - 0.1f) * 10.0f % 1.0f);
        } else if (easedProgress >= 0.2f && easedProgress < 0.3f) {
            return lerp(2.0f, -4.0f, (easedProgress - 0.2f) * 10.0f % 1.0f);
        } else if (easedProgress >= 0.3f && easedProgress < 0.4f) {
            return lerp(-4.0f, 4.0f, (easedProgress - 0.3f) * 10.0f % 1.0f);
        } else if (easedProgress >= 0.4f && easedProgress < 0.6f) {
            return 4.0f;
        } else if (easedProgress >= 0.6f && easedProgress < 0.7f) {
            return lerp(4.0f, -4.0f, (easedProgress - 0.6f) * 10.0f % 1.0f);
        } else if (easedProgress >= 0.7f && easedProgress < 0.8f) {
            return lerp(-4.0f, 2.0f, (easedProgress - 0.7f) * 10.0f % 1.0f);
        } else if (easedProgress >= 0.8f && easedProgress < 0.9f) {
            return lerp(2.0f, -1.0f, (easedProgress - 0.8f) * 10.0f % 1.0f);
        }

        return 0.0f;
    }

    private float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }


    public boolean isAnimating() {
        return animating;
    }
}
