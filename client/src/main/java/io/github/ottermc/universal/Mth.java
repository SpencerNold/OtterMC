package io.github.ottermc.universal;

public class Mth {

    public static float clamp(float f, float min, float max) {
        if (f < min)
            return min;
        else if (f > max)
            return max;
        return f;
    }
}
