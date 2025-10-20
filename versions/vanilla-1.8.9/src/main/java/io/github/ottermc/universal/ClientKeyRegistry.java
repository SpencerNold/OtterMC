package io.github.ottermc.universal;

import org.lwjgl.input.Keyboard;

public class ClientKeyRegistry extends UKeyRegistry {
    @Override
    protected int _getKeyC() {
        return Keyboard.KEY_C;
    }

    @Override
    protected int _getKeyLShift() {
        return Keyboard.KEY_LSHIFT;
    }

    @Override
    protected int _getKeyRShift() {
        return Keyboard.KEY_RSHIFT;
    }
}
