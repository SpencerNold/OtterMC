package io.github.fabric.universal.hud;

import io.github.fabric.mixin.KeyBindingAccessor;
import io.github.fabric.universal.GLFWKeyboard;
import io.github.ottermc.modules.impl.hud.KeyStroke;
import io.github.ottermc.render.hud.impl.KeyStrokeHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class ClientKeyStrokeHud extends KeyStrokeHud {

    public ClientKeyStrokeHud() {
        super(61, 54);
    }

    @Override
    public void draw(Object context) {
        drawKeys((DrawContext) context, MinecraftClient.getInstance(), true);
    }

    @Override
    public void drawDummyObject(Object context) {
        drawKeys((DrawContext) context, MinecraftClient.getInstance(), false);
    }

    private void drawKeys(DrawContext context, MinecraftClient mc, boolean clickColor) {
        clickColor = clickColor && mc.currentScreen == null;
        int color = KeyStroke.getColor().getValue();
        KeyObject object;
        // w
        object = new KeyObject(mc.options.forwardKey, getDefaultX() + 21, getDefaultY(), 19, 19, true);
        object.draw(context, mc, color, clickColor);
        // a
        object = new KeyObject(mc.options.leftKey, getDefaultX(), getDefaultY() + 21, 19, 19, true);
        object.draw(context, mc, color, clickColor);
        // s
        object = new KeyObject(mc.options.backKey, getDefaultX() + 21, getDefaultY() + 21, 19, 19, true);
        object.draw(context, mc, color, clickColor);
        // d
        object = new KeyObject(mc.options.rightKey, getDefaultX() + 42, getDefaultY() + 21, 19, 19, true);
        object.draw(context, mc, color, clickColor);
        // space
        object = new KeyObject(mc.options.jumpKey, getDefaultX(), getDefaultY() + 42, 61, 12, false);
        object.draw(context, mc, color, clickColor);
        context.fill(object.x + 12, object.y + 6, object.x + object.width - 12, object.y + 7, color);
    }

    private record KeyObject(KeyBinding binding, int x, int y, int width, int height, boolean hasText) {

        private void draw(DrawContext context, MinecraftClient mc, int color, boolean clickColor) {
            int key = getBoundKey(binding).getCode();
            int background = (GLFWKeyboard.isKeyDown(key) && clickColor) ? 0x66ffffff : 0x90000000;
            context.fill(x, y, x + width, y + height, background);
            if (hasText)
                drawText(context, mc, color);
        }

        private void drawText(DrawContext context, MinecraftClient mc, int color) {
            String text = binding.getBoundKeyLocalizedText().getString().toUpperCase();
            text = text.substring(1) + text.toLowerCase();
            TextRenderer renderer = mc.textRenderer;
            float x = this.x + (width / 2.0f) - (renderer.getWidth(text) / 2.0f);
            float y = this.y + (height / 2.0f) - (renderer.fontHeight / 2.0f);
            context.drawText(renderer, text, (int) x + 1, (int) y + 1, color, false);
        }

        private InputUtil.Key getBoundKey(KeyBinding binding) {
            return ((KeyBindingAccessor) binding).getBoundKey();
        }
    }
}
