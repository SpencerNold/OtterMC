package io.github.ottermc.keybind;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GLFWKeyboard extends UniversalKeyboard {

    @Override
    protected String keyToName(int key) {
        return InputUtil.fromKeyCode(new KeyInput(key, 0, 0)).getTranslationKey();
    }

    @Override
    protected int nameToKey(String name) {
        return InputUtil.fromTranslationKey(name).getCode();
    }

    @Override
    protected boolean keyDown(int key) {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
    }
}
