package io.github.ottermc.screen.hud;

import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public abstract class Component {

	protected final DrawableHelper drawable = new DrawableHelper();
	
	protected int x, y, width, height;
	
	protected float scale = 1.0f;
	protected boolean visible;
	
	public Component(boolean visible) {
		this.visible = visible;
	}
	
	public void drawComponent(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (!visible)
			return;
		GlStateManager.scale(scale, scale, scale);
		float inv = 1.0f / scale;
		float x = getX() - (getX() * inv);
		float y = getY() - (getY() * inv);
		GlStateManager.translate(-x, -y, 0);
		if (!(this instanceof Movable) || !mc.gameSettings.showDebugInfo)
			draw(mc, gui, res, partialTicks);
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(inv, inv, inv);
	}
	
	protected abstract void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks);
	
	public float getScale() {
		return scale;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRawWidth() {
		return width;
	}
	
	public int getWidth() {
		return (int) (width * scale);
	}
	
	public int getRawHeight() {
		return height;
	}
	
	public int getHeight() {
		return (int) (height * scale);
	}
}
