package io.github.ottermc.screen.hud.minecraft;

import agent.Reflection;
import io.github.ottermc.screen.hud.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MinecraftChatHud extends Component {

	public MinecraftChatHud() {
		super(true);
	}
	
	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float)(res.getScaledHeight() - 48), 0.0F);
        mc.mcProfiler.startSection("chat");
        this.getPersistantChatGUI(gui).drawChat(this.getUpdateCounter(gui));
        mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
	}
	
	public GuiNewChat getPersistantChatGUI(GuiIngame gui) {
		return (GuiNewChat) Reflection.getMinecraftField("net/minecraft/client/gui/GuiIngame", "persistantChatGUI", gui);
	}
	
	private int getUpdateCounter(GuiIngame gui) {
		return (int) Reflection.getMinecraftField("net/minecraft/client/gui/GuiIngame", "updateCounter", gui);
	}
}
