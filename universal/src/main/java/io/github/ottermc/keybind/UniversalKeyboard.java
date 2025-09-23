package io.github.ottermc.keybind;

public abstract class UniversalKeyboard {

    private static UniversalKeyboard instance = null;

    public static void register(UniversalKeyboard keyboard) {
        instance = keyboard;
    }

    public static String translateKeyToName(int key) {
        if (instance == null)
            throw new IllegalStateException("UniversalKeyboard must be registered before it can be used");
        return instance.keyToName(key);
    }

    public static int translateNameToKey(String name) {
        if (instance == null)
            throw new IllegalStateException("UniversalKeyboard must be registered before it can be used");
        return instance.nameToKey(name);
    }

    public static boolean isKeyDown(int key) {
        if (instance == null)
            throw new IllegalStateException("UniversalKeyboard must be registered before it can be used");
        return instance.keyDown(key);
    }

    protected abstract String keyToName(int key);
    protected abstract int nameToKey(String name);
    protected abstract boolean keyDown(int key);
}
