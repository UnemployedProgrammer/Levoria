package com.sebastian.levoria.effects;

public class ScreenShakeEffect {
    //Instance for easy access
    public static ScreenShakeEffect INSTANCE;

    //Actual Content

    private int shakeDuration = 0;
    private float shakeIntensity = 1;

    public void shakeScreen(int ticks) {
        shakeDuration = ticks;
    }

    public void shakeScreen(int ticks, float intensity) {
        shakeDuration = ticks;
        setShakeIntensity(intensity);
    }


    public boolean isShaking() {
        return shakeDuration > 0;
    }

    public void setShakeIntensity(float shakeIntensity) {
        this.shakeIntensity = shakeIntensity;
    }

    public float getShakeIntensity() {
        return shakeIntensity;
    }

    public void updateShake() {
        if (shakeDuration > 0) {
            shakeDuration--;
        }
    }

}
