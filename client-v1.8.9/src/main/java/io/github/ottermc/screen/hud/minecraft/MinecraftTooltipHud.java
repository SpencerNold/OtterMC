package io.github.ottermc.screen.hud.minecraft;

import io.github.ottermc.screen.hud.Component;
import me.spencernold.transformer.Reflection;
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
		return (GuiSpectator) Reflection.getValue(GuiIngame.class, gui, "spectatorGui");
	}

	private void renderTooltip(GuiIngame gui, ScaledResolution res, float partialTicks) {
		Reflection.call(GuiIngame.class, gui, "renderTooltip", "(Lnet/minecraft/client/gui/ScaledResolution;F)V", res, partialTicks);
	}

	private boolean showCrosshair(GuiIngame gui) {
		return (boolean) Reflection.call(GuiIngame.class, gui, "showCrosshair", "()Z");
	}

	public void drawTexturedModalRect(Gui gui, int x, int y, int textureX, int textureY, int width, int height) {
		Reflection.call(Gui.class, gui, "drawTexturedModalRect", "(IIIIII)V", x, y, textureX, textureY, width, height);
	}
}
