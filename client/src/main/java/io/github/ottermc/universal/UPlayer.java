package io.github.ottermc.universal;

public abstract class UPlayer {

    private static UPlayer instance;

    public static void register(UPlayer minecraft) {
        instance = minecraft;
    }

    public static boolean isSprintViable() {
        assertNotNull(instance);
        return instance._isSprintViable();
    }

    public static void setSprinting(boolean sprinting) {
        assertNotNull(instance);
        instance._setSprinting(sprinting);
    }

    public abstract boolean _isSprintViable();
    public abstract void _setSprinting(boolean sprinting);

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("Player must be registered before it can be used");
    }
}
