package io.github.ottermc.universal;

public abstract class UGameSettings {

    private static UGameSettings instance;

    public static void register(UGameSettings game) {
        instance = game;
    }

    public static void setGamma(float gamma) {
        assertNotNull(instance);
        instance._setGamma(gamma);
    }

    public static void setSmoothCamera(boolean smooth) {
        assertNotNull(instance);
        instance._setSmoothCamera(smooth);
    }

    public static void setFieldOfView(float fov) {
        assertNotNull(instance);
        instance._setFieldOfView(fov);
    }

    public static float getFieldOfView() {
        assertNotNull(instance);
        return instance._getFieldOfView();
    }

    public static boolean isSprintKeyDown() {
        assertNotNull(instance);
        return instance._isSprintKeyDown();
    }

    protected abstract void _setGamma(float gamma);
    protected abstract void _setSmoothCamera(boolean smooth);
    protected abstract void _setFieldOfView(float fov);
    protected abstract float _getFieldOfView();
    protected abstract boolean _isSprintKeyDown();

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("GameSettings must be registered before it can be used");
    }
}
