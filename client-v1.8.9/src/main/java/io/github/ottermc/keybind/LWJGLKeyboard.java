package io.github.ottermc.keybind;

import io.github.ottermc.UniversalKeyboard;
import org.lwjgl.input.Keyboard;

public class LWJGLKeyboard extends UniversalKeyboard {

    @Override
    protected String keyToName(int key) {
        return Keyboard.getKeyName(key);
    }

    @Override
    protected int nameToKey(String name) {
        return Keyboard.getKeyIndex(name);
    }

    @Override
    protected boolean keyDown(int key) {
        return Keyboard.isKeyDown(key);
    }
}
