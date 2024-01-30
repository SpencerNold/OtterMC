package io.github.ottermc.screen.hud.minecraft;

import agent.Reflection;
import io.github.ottermc.screen.hud.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MinecraftTooltipHud extends Component {

	public MinecraftTooltipHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (mc.playerController.isSpectator()) {
			this.getSpectatorGui(gui).renderTooltip(res, partialTicks);
		} else {
			this.renderTooltip(gui, res, partialTicks);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(Gui.icons);
		GlStateManager.enableBlend();

		if (this.showCrosshair(gui)) {
			int i = res.getScaledWidth();
			int j = res.getScaledHeight();
			GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
			GlStateManager.enableAlpha();
			drawTexturedModalRect(gui, i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
		}
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	}

	private GuiSpectator getSpectatorGui(GuiIngame gui) {
		return (GuiSpectator) Reflection.getMinecraftField("net/minecraft/client/gui/GuiIngame", "spectatorGui", gui);
	}

	private void renderTooltip(GuiIngame gui, ScaledResolution res, float partialTicks) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/GuiIngame", "renderTooltip(Lnet/minecraft/client/gui/ScaledResolution;F)V", gui, res, partialTicks);
	}

	private boolean showCrosshair(GuiIngame gui) {
		return (boolean) Reflection.invokeMinecraft("net/minecraft/client/gui/GuiIngame", "showCrosshair()Z", gui);
	}

	public void drawTexturedModalRect(Gui gui, int x, int y, int textureX, int textureY, int width, int height) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/Gui", "drawTexturedModalRect(IIIIII)V", gui, x, y, textureX, textureY, width, height);
	}
}
