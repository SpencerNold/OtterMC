package io.github.ottermc.hud.client;

import io.github.ottermc.modules.hud.KeyStroke;
import io.github.ottermc.screen.hud.MovableComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.input.Keyboard;

public class ClientKeyStrokeHud extends MovableComponent {

    public ClientKeyStrokeHud() {
        super(10, 10, 58, 51);
    }

    @Override
    protected void draw(Minecraft mc, GuiIngame gui, float partialTicks) {
        drawKeys(mc, true);
    }

    @Override
    public void drawDummyObject(Minecraft mc, Gui gui, float partialTicks) {
        drawKeys(mc, false);
    }

    private void drawKeys(Minecraft mc, boolean clickColor) {
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

    @Override
    public int getSerialId() {
        return "KEYSTROKE_COMPONENT".hashCode();
    }
}
