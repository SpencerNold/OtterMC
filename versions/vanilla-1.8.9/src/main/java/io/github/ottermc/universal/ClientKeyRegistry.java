package io.github.ottermc.universal;

import org.lwjgl.input.Keyboard;

public class ClientKeyRegistry extends UKeyRegistry {
    @Override
    protected int getRegKeyC() {
        return Keyboard.KEY_C;
    }
}
