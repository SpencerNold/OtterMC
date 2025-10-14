package io.github.ottermc.universal;

public abstract class UDrawable {

    private static UDrawable instance;

    public static void register(UDrawable drawable) {
        instance = drawable;
    }

    public static void push(Object context) {
        assertNotNull(instance);
        instance._push(context);
    }

    public static void pop(Object context) {
        assertNotNull(instance);
        instance._pop(context);
    }

    public static void translate(Object context, float x, float y) {
        assertNotNull(instance);
        instance._translate(context, x, y);
    }

    public static void scale(Object context, float x, float y) {
        assertNotNull(instance);
        instance._scale(context, x, y);
    }

    protected abstract void _push(Object context);
    protected abstract void _pop(Object context);
    protected abstract void _translate(Object context, float x, float y);
    protected abstract void _scale(Object context, float x, float y);

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("GameSettings must be registered before it can be used");
    }
}
