package io.github.ottermc.universal;

public abstract class UVersion {

    public static final int V8 = 8;
    public static final int V21 = 21;

    private static UVersion instance;

    public static void register(UVersion version) {
        instance = version;
    }

    public static int getClientVersion() {
        assertNotNull(instance);
        return instance._getClientVersion();
    }

    protected abstract int _getClientVersion();

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("Version must be registered before it can be used");
    }
}
