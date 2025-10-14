package io.github.ottermc.universal;

import io.github.ottermc.universal.UKeyboard;

public class LWJGLKeyboard extends UKeyboard {

    @Override
    protected String keyToName(int key) {
        return org.lwjgl.input.Keyboard.getKeyName(key);
    }

    @Override
    protected int nameToKey(String name) {
        return org.lwjgl.input.Keyboard.getKeyIndex(name);
    }

    @Override
    protected boolean keyDown(int key) {
        return org.lwjgl.input.Keyboard.isKeyDown(key);
    }
}
