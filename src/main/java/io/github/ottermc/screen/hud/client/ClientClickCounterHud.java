package io.github.ottermc.screen.hud.client;

import java.util.ArrayList;
import java.util.List;

import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;

public class ClientClickCounterHud extends Component implements Movable {
	
	private static final ClickCounter LEFT;
	private static final ClickCounter RIGHT;
	
	static {
		Minecraft mc = Minecraft.getMinecraft();
		LEFT = new ClickCounter(mc.gameSettings.keyBindAttack);
		RIGHT = new ClickCounter(mc.gameSettings.keyBindUseItem);
	}
	
	public ClientClickCounterHud() {
		super(false);
		x = -1;
		y = 57;
		width = 58;
		height = 18;
	}
	
	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		int color = io.github.ottermc.modules.hud.ClickCounter.getColor().getValue();
		LEFT.update();
		LEFT.draw(mc, drawable, getX(), getY(), 28, getRawHeight(), color);
		RIGHT.update();
		RIGHT.draw(mc, drawable, getX() + 30, getY(), 28, getRawHeight(), color);
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
	public int getRawWidth() {
		return width;
	}
	
	@Override
	public int getRawHeight() {
		return height;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	private static class ClickCounter {
		
		final List<Long> clicks = new ArrayList<>();
		final KeyBinding key;
		boolean wasPressed;
		long lastPressed;
		
		ClickCounter(KeyBinding key) {
			this.key = key;
		}
		
		void draw(Minecraft mc, DrawableHelper drawable, int x, int y, int width, int height, int color) {
			drawable.fillRectangle(x, y, width, height, (key.isKeyDown() && mc.currentScreen == null) ? 0x66ffffff : 0x90000000);
			String text = this.toString();
			mc.fontRendererObj.drawString(text, x + 14 - mc.fontRendererObj.getStringWidth(text) * 0.5f, y + 5, color, false);
		}
		
		void update() {
			if (Minecraft.getMinecraft().currentScreen != null)
				return;
			boolean pressed = key.isKeyDown();
			if (pressed != wasPressed) {
				lastPressed = System.currentTimeMillis();
				wasPressed = pressed;
				if (pressed)
					clicks.add(lastPressed);
			}
		}
		
		int getClicks() {
			final long time = System.currentTimeMillis();
			clicks.removeIf(l -> l + 1000 < time);
			int count = clicks.size();
			return count >= 8 ? count + 1 : count;
		}
		
		public String toString() {
			return getClicks() < 10 ? "0" + getClicks() : getClicks() + "";
		}
	}
}
