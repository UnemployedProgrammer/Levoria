package com.sebastian.levoria.effects;

public class ScreenShakeEffect {
    //Instance for easy access
    public static ScreenShakeEffect INSTANCE;

    //Actual Content

    private int shakeDuration = 0;

    public void shakeScreen(int ticks) {
        shakeDuration = ticks;
    }

    public boolean isShaking() {
        return shakeDuration > 0;
    }

    public void updateShake() {
        if (shakeDuration > 0) {
            shakeDuration--;
        }
    }

}
