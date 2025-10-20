package io.github.ottermc.universal;

import io.github.ottermc.render.screen.AbstractScreen;

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

    public static void color(float r, float g, float b, float a) {
        assertNotNull(instance);
    }

    public static void drawString(Object context, String text, int x, int y, int color, boolean shadow) {
        assertNotNull(instance);
        instance._drawString(context, text, x, y, color, shadow);
    }

    public static void fillRect(Object context, int x, int y, int width, int height, int color) {
        assertNotNull(instance);
        instance._fillRect(context, x, y, width, height, color);
    }

    public static void outlineRect(Object context, int x, int y, int width, int height, int color) {
        assertNotNull(instance);
        instance._outlineRect(context, x, y, width, height, color);
    }

    public static void display(AbstractScreen screen) {
        assertNotNull(instance);
        instance._display(screen);
    }

    public static int getScaledWidth() {
        assertNotNull(instance);
        return instance._getScaledWidth();
    }

    public static int getScaledHeight() {
        assertNotNull(instance);
        return instance._getScaledHeight();
    }

    public static float getStringWidth(String text) {
        assertNotNull(instance);
        return instance._getStringWidth(text);
    }

    public static float getStringHeight() {
        assertNotNull(instance);
        return instance._getStringHeight();
    }

    protected abstract void _push(Object context);
    protected abstract void _pop(Object context);
    protected abstract void _translate(Object context, float x, float y);
    protected abstract void _scale(Object context, float x, float y);
    protected abstract void _color(float r, float g, float b, float a);
    protected abstract void _fillRect(Object context, int x, int y, int width, int height, int color);
    protected abstract void _outlineRect(Object context, int x, int y, int width, int height, int color);
    protected abstract void _drawString(Object context, String text, int x, int y, int color, boolean shadow);
    protected abstract int _getScaledWidth();
    protected abstract int _getScaledHeight();
    protected abstract float _getStringWidth(String text);
    protected abstract float _getStringHeight();

    protected abstract void _display(AbstractScreen screen);

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("GameSettings must be registered before it can be used");
    }
}
