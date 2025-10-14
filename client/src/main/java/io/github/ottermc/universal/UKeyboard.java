package io.github.ottermc.universal;

public abstract class UKeyboard {

    private static UKeyboard instance = null;

    public static void register(UKeyboard keyboard) {
        instance = keyboard;
    }

    public static String translateKeyToName(int key) {
        assertNotNull(instance);
        return instance.keyToName(key);
    }

    public static int translateNameToKey(String name) {
        assertNotNull(instance);
        return instance.nameToKey(name);
    }

    public static boolean isKeyDown(int key) {
        assertNotNull(instance);
        return instance.keyDown(key);
    }

    protected abstract String keyToName(int key);
    protected abstract int nameToKey(String name);
    protected abstract boolean keyDown(int key);

    private static void assertNotNull(Object object) {
        if (object == null)
            throw new IllegalStateException("Keyboard must be registered before it can be used");
    }
}
