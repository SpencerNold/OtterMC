package io.github.ottermc.universal;

public abstract class UKeyRegistry {

    private static UKeyRegistry instance;

    public static void register(UKeyRegistry registry) {
        instance = registry;
    }

    public static int getKeyC() {
        assertNotNull(instance);
        return instance._getKeyC();
    }

    public static int getKeyLShift() {
        assertNotNull(instance);
        return instance._getKeyLShift();
    }

    public static int getKeyRShift() {
        assertNotNull(instance);
        return instance._getKeyRShift();
    }

    protected abstract int _getKeyC();
    protected abstract int _getKeyLShift();
    protected abstract int _getKeyRShift();

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("KeyRegistry must be registered before it can be used");
    }
}
