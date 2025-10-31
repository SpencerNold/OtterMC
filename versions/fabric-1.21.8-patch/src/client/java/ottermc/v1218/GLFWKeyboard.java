package ottermc.v1218;

import net.minecraft.client.util.InputUtil;

public class GLFWKeyboard extends io.github.fabric.universal.GLFWKeyboard {
    @Override
    protected String keyToName(int key) {
        return InputUtil.fromKeyCode(key, 0).getTranslationKey();
    }
}
