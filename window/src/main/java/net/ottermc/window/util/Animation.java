package net.ottermc.window.util;

import javax.swing.*;

public class Animation {

    private final float min, max;
    private final int delay;
    private final Runnable updater;
    private float value;
    private float target;
    private Timer timer;

    public Animation(float min, float max, int delay, Runnable updater) {
        this.min = min;
        this.max = max;
        this.delay = delay;
        this.updater = updater;
        this.value = min;
        this.target = min;
    }

    public void start(float target) {
        this.target = target;
        if (timer != null && timer.isRunning())
            timer.stop();
        timer = new Timer(delay, e -> {
            float speed = 0.15f;
            value += (target - value) * speed;
            if (Math.abs(value - target) < 0.001f) {
                value = target;
                timer.stop();
            }
            updater.run();;
        });
        timer.start();
    }

    public float getValue() {
        return value;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public float getTarget() {
        return target;
    }
}
