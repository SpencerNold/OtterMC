package io.github.ottermc.universal;

import org.lwjgl.glfw.GLFW;

public class ClientKeyRegistry extends UKeyRegistry {
    @Override
    protected int _getKeyC() {
        return GLFW.GLFW_KEY_C;
    }

    @Override
    protected int _getKeyLShift() {
        return GLFW.GLFW_KEY_LEFT_SHIFT;
    }

    @Override
    protected int _getKeyRShift() {
        return GLFW.GLFW_KEY_RIGHT_SHIFT;
    }
}
