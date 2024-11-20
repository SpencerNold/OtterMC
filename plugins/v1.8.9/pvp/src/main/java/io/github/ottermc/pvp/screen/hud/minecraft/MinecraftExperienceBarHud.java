package io.github.ottermc.pvp.screen.hud.minecraft;

import agent.Reflection;
import io.github.ottermc.screen.hud.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MinecraftExperienceBarHud extends Component {

	public MinecraftExperienceBarHud() {
		super(true);
	}
	
	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i = res.getScaledWidth();
        int k1 = i / 2 - 91;

        if (mc.thePlayer.isRidingHorse())
        {
            this.renderHorseJumpBar(gui, res, k1);
        }
        else if (mc.playerController.gameIsSurvivalOrAdventure())
        {
            this.renderExpBar(gui, res, k1);
        }
	}
	
	private void renderExpBar(GuiIngame gui, ScaledResolution res, int k1) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/GuiIngame", "renderExpBar(Lnet/minecraft/client/gui/ScaledResolution;I)V", gui, res, k1);
	}

	private void renderHorseJumpBar(GuiIngame gui, ScaledResolution res, int k1) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/GuiIngame", "renderHorseJumpBar(Lnet/minecraft/client/gui/ScaledResolution;I)V", gui, res, k1);
	}
}
