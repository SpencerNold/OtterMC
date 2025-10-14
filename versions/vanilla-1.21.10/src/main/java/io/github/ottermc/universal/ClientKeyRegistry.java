package io.github.ottermc.universal;

import org.lwjgl.glfw.GLFW;

public class ClientKeyRegistry extends UKeyRegistry {
    @Override
    protected int getRegKeyC() {
        return GLFW.GLFW_KEY_C;
    }
}
