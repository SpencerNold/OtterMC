package io.github.ottermc.screen.hud.client;

import org.lwjgl.opengl.Display;

import io.github.ottermc.modules.hud.Direction;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

// TODO This class needs work, but I'm sick of it
public class ClientDirectionHud extends Component implements Movable {
	
	public ClientDirectionHud() {
		super(false);
		width = 5 * 7 * 5 + 12;
		height = 20;
		x = -1;
		y = 2;
	}
	
	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (!Display.isFullscreen())
			return;
		boolean ttf = Direction.shouldUseClientFont();
		Color color = Direction.getColor();
		
		drawable.fillRectangle(getX(), getY(), getWidth(), getHeight(), 0x400D1A22);
		
		Entity player = mc.thePlayer;
		float angle = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 180;
		float n = angle / 5.0f;
		
		final int size = 5;
		int[] bounds = new int[size];
		int i;
		for (i = 0; i < size; i++) {
			bounds[i] = MathHelper.ceiling_float_int(n) * 5 + ((i - 2) * 5);
		}
		int x = getX() + drawable.middle(getWidth(), 0) - 24;
		for (i = 0; i < size; i++) {
			String text;
			if (bounds[i] == 0 || bounds[i] == 360)
				text = "N";
			else if (bounds[i] == 90)
				text = "E";
			else if (bounds[i] == 180)
				text = "S";
			else if (bounds[i] == 270)
				text = "W";
			else if (bounds[i] >= 360)
				text = String.valueOf(bounds[i] - 360);
			else if (bounds[i] < 0)
				text = String.valueOf(bounds[i] + 360);
			else
				text = String.valueOf(bounds[i]);
			float xPos = x + (bounds[i] - angle) * 7;
			if (ttf)
				drawable.drawString(text, (int) xPos, getY() + 10, color.getValue(0xFF - (Math.abs(i - 2)) * 50));
			else
				mc.fontRendererObj.drawString(text, xPos, getY() + 10, color.getValue(0xFF), false);
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
		return x == -1 ? x = drawable.middle((int) drawable.getWidth(), getRawWidth()) : x;
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
		return "DIRECTION_COMPONENT".hashCode();
	}
}
