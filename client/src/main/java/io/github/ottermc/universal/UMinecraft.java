package io.github.ottermc.universal;

public abstract class UMinecraft {

    private static UMinecraft instance;

    public static void register(UMinecraft minecraft) {
        instance = minecraft;
    }

    public static Object getCurrentScreen() {
        assertNotNull(instance);
        return instance._getCurrentScreen();
    }

    protected abstract Object _getCurrentScreen();

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("Minecraft must be registered before it can be used");
    }
}
