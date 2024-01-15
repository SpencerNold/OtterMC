package io.github.ottermc.screen.hud.client;

import io.github.ottermc.modules.hud.Coordinate;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class ClientCoordinateHud extends Component implements Movable {
	
	public ClientCoordinateHud() {
		super(false);
		x = 4;
		y = 4;
	}

	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		Entity player = mc.thePlayer;
		String text = String.format("X: %.1f, Y: %.1f, Z: %.1f (%s)", player.posX, player.posY, player.posZ, EnumFacing.fromAngle(player.rotationYaw).getName());
		boolean ttf = Coordinate.shouldUseClientFont();
		int color = Coordinate.getColor().getValue();
		if (ttf) {
			width = drawable.getStringWidth(text);
			height = drawable.getStringHeight();
			drawable.drawString(text, getX() + 1, getY() + 1, color);
		} else {
			width = mc.fontRendererObj.getStringWidth(text);
			height = mc.fontRendererObj.FONT_HEIGHT;
			mc.fontRendererObj.drawString(text, getX() + 1, getY() + 1, color);
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
}
