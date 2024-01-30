package io.github.ottermc.screen.hud.client;

import org.lwjgl.opengl.Display;

import io.github.ottermc.modules.hud.Array;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

public class ClientArrayHud extends Component implements Movable {

	public ClientArrayHud() {
		super(false);
	}
	
	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (!Display.isFullscreen())
			return;
		Color color = Array.getColor();
		boolean ttf = Array.shouldUseClientFont();
		String[] lines = Array.getText().split("\n");
		height = lines.length * (ttf ? drawable.getStringHeight() : mc.fontRendererObj.FONT_HEIGHT + 2);
		for (int i = 0; i < lines.length; i++) {
			String text = lines[i];
			if (ttf) {
				width = Math.max(width, drawable.getStringWidth(text));
				drawable.drawString(text, getX(), getY() + (i * (drawable.getStringHeight() + 2)), color.getValue());
			} else {
				width = Math.max(width, mc.fontRendererObj.getStringWidth(text));
				mc.fontRendererObj.drawString(text, getX(), getY() + (i * (mc.fontRendererObj.FONT_HEIGHT + 2)), color.getValue());
			}
		}
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
		return x;
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
		return "ARRAY_COMPONENT".hashCode();
	}
}
