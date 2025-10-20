package io.github.ottermc.universal;

public abstract class Mth {

    private static Mth instance;

    public static void register(Mth mth) {
        instance = mth;
    }

    public static float sin(float value) {
        assertNotNull(instance);
        return instance._sin(value);
    }

    protected abstract float _sin(float value);

    public static float clamp(float f, float min, float max) {
        if (f < min)
            return min;
        else if (f > max)
            return max;
        return f;
    }

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("Math Helper must be registered before it can be used");
    }
}
