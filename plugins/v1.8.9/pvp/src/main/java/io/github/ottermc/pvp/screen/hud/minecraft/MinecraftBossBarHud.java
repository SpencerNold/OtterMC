package io.github.ottermc.pvp.screen.hud.minecraft;

import agent.Reflection;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

public class MinecraftBossBarHud extends Component implements Movable {
	
	public MinecraftBossBarHud() {
		super(true);
		x = -1;
		y = 2;
		width = 182;
		height = 15;
	}
	
	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
        mc.mcProfiler.startSection("bossHealth");
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		mc.getTextureManager().bindTexture(Gui.icons);
        this.renderBossHealth(gui);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
	}
	
	private void renderBossHealth(GuiIngame gui) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/GuiIngame", "renderBossHealth()V", gui);
	}
	
	@Override
	public int getX() {
		if (x == -1) {
			ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
			return x = (res.getScaledWidth() / 2 - 91);
		}
		return x;
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
	
	@Override
	public boolean isVisible() {
		return super.isVisible() && BossStatus.healthScale > 0;
	}
	
	@Override
	public int getSerialId() {
		return "BOSSBAR_COMPONENT".hashCode();
	}
}
