package io.github.ottermc.universal.hud;

import io.github.ottermc.modules.impl.hud.KeyStroke;
import io.github.ottermc.render.hud.impl.KeyStrokeHud;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.universal.Type;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ClientKeyStrokeHud extends KeyStrokeHud {

    @Override
    public void draw(@Type(DrawableHelper.class) Object context) {
        drawKeys((DrawableHelper) context, Minecraft.getMinecraft(), true);
    }

    @Override
    public void drawDummyObject(@Type(DrawableHelper.class) Object context) {
        drawKeys((DrawableHelper) context, Minecraft.getMinecraft(), false);
    }

    private void drawKeys(DrawableHelper drawable, Minecraft mc, boolean clickColor) {
        clickColor = clickColor && mc.currentScreen == null;
        int color = KeyStroke.getColor().getValue();
        int key;
        String text;
        // w
        key = mc.gameSettings.keyBindForward.getKeyCode();
        drawable.fillRectangle(getDefaultX() + 20, getDefaultY(), 18, 18, (Keyboard.isKeyDown(key) && clickColor) ? 0x66ffffff : 0x90000000);
        text = Keyboard.getKeyName(key).toUpperCase();
        mc.fontRendererObj.drawString(text, getDefaultX() + 23.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getDefaultY() + 5, color, false);
        // a
        key = mc.gameSettings.keyBindLeft.getKeyCode();
        drawable.fillRectangle(getDefaultX(), getDefaultY() + 20, 18, 18, (Keyboard.isKeyDown(key) && clickColor) ? 0x66ffffff : 0x90000000);
        text = Keyboard.getKeyName(key).toUpperCase();
        mc.fontRendererObj.drawString(text, getDefaultX() + 3.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getDefaultY() + 25, color, false);
        // s
        key = mc.gameSettings.keyBindBack.getKeyCode();
        drawable.fillRectangle(getDefaultX() + 20, getDefaultY() + 20, 18, 18, (Keyboard.isKeyDown(key) && clickColor) ? 0x66ffffff : 0x90000000);
        text = Keyboard.getKeyName(key).toUpperCase();
        mc.fontRendererObj.drawString(text, getDefaultX() + 23.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getDefaultY() + 25, color, false);
        // d
        key = mc.gameSettings.keyBindRight.getKeyCode();
        drawable.fillRectangle(getDefaultX() + 40, getDefaultY() + 20, 18, 18, (Keyboard.isKeyDown(key) && clickColor) ? 0x66ffffff : 0x90000000);
        text = Keyboard.getKeyName(key).toUpperCase();
        mc.fontRendererObj.drawString(text, getDefaultX() + 43.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getDefaultY() + 25, color, false);
        // space
        key = mc.gameSettings.keyBindJump.getKeyCode();
        drawable.fillRectangle(getDefaultX(), getDefaultY() + 40, getRawWidth(), 11, (Keyboard.isKeyDown(key) && clickColor) ? 0x66ffffff : 0x90000000);
        drawable.drawHorizontalLine(getDefaultX() + 20, getDefaultX() + getRawWidth() - 20, getDefaultY() + 44, color);
    }
}
