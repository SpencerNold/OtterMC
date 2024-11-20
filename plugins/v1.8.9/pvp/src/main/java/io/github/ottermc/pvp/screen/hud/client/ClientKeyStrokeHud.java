package io.github.ottermc.pvp.screen.hud.client;

import io.github.ottermc.pvp.modules.hud.KeyStroke;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class ClientKeyStrokeHud extends Component implements Movable {
	
	public ClientKeyStrokeHud() {
		super(false);
		x = -1;
		y = 4;
		width = 58;
		height = 51;
	}
	
	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (!Display.isFullscreen())
			return;
		int color = KeyStroke.getColor().getValue();
		int key;
		String text;
		// w
		key = mc.gameSettings.keyBindForward.getKeyCode();
		drawable.fillRectangle(getX() + 20, getY(), 18, 18, Keyboard.isKeyDown(key) ? 0x66ffffff : 0x90000000);
		text = Keyboard.getKeyName(key).toUpperCase();
		mc.fontRendererObj.drawString(text, getX() + 23.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getY() + 5, color, false);
		// a
		key = mc.gameSettings.keyBindLeft.getKeyCode();
		drawable.fillRectangle(getX(), getY() + 20, 18, 18, Keyboard.isKeyDown(key) ? 0x66ffffff : 0x90000000);
		text = Keyboard.getKeyName(key).toUpperCase();
		mc.fontRendererObj.drawString(text, getX() + 3.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getY() + 25, color, false);
		// s
		key = mc.gameSettings.keyBindBack.getKeyCode();
		drawable.fillRectangle(getX() + 20, getY() + 20, 18, 18, Keyboard.isKeyDown(key) ? 0x66ffffff : 0x90000000);
		text = Keyboard.getKeyName(key).toUpperCase();
		mc.fontRendererObj.drawString(text, getX() + 23.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getY() + 25, color, false);
		// d
		key = mc.gameSettings.keyBindRight.getKeyCode();
		drawable.fillRectangle(getX() + 40, getY() + 20, 18, 18, Keyboard.isKeyDown(key) ? 0x66ffffff : 0x90000000);
		text = Keyboard.getKeyName(key).toUpperCase();
		mc.fontRendererObj.drawString(text, getX() + 43.5f + mc.fontRendererObj.getStringWidth(text) * 0.5f, getY() + 25, color, false);
		// space
		key = mc.gameSettings.keyBindJump.getKeyCode();
		drawable.fillRectangle(getX(), getY() + 40, getRawWidth(), 11, Keyboard.isKeyDown(key) ? 0x66ffffff : 0x90000000);
		drawable.drawHorizontalLine(getX() + 20, getX() + getRawWidth() - 20, getY() + 44, color);
	}

	@Override
	public int getRawWidth() {
		return width;
	}

	@Override
	public int getRawHeight() {
		return height;
	}

	@Override
	public int getX() {
		return x == -1 ? x = (int) drawable.getWidth() - getRawWidth() - 4 : x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public int getSerialId() {
		return "KEYSTROKE_COMPONENT".hashCode();
	}
}
