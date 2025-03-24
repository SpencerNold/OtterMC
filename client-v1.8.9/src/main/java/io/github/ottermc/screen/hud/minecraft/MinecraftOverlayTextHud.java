package io.github.ottermc.screen.hud.minecraft;

import io.github.ottermc.screen.hud.Component;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class MinecraftOverlayTextHud extends Component {
	
	public MinecraftOverlayTextHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (mc.gameSettings.heldItemTooltips && !mc.playerController.isSpectator()) {
			this.func_181551_a(gui, res);
		} else if (mc.thePlayer.isSpectator()) {
			this.getSpectatorGui(gui).func_175263_a(res);
		}

		if (mc.gameSettings.showDebugInfo) {
			getOverlayDebug(gui).renderDebugInfo(res);
		}

		if (this.getRecordPlayingUpFor(gui) > 0) {
			mc.mcProfiler.startSection("overlayMessage");
			float f2 = (float) this.getRecordPlayingUpFor(gui) - partialTicks;
			int l1 = (int) (f2 * 255.0F / 20.0F);

			if (l1 > 255) {
				l1 = 255;
			}

			if (l1 > 8) {
				GlStateManager.pushMatrix();
		        int i = res.getScaledWidth();
		        int j = res.getScaledHeight();
				GlStateManager.translate((float) (i / 2), (float) (j - 68), 0.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				int l = 16777215;

				if (this.getRecordIsPlaying(gui)) {
					l = MathHelper.func_181758_c(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				mc.fontRendererObj.drawString(this.getRecordPlaying(gui),
						-mc.fontRendererObj.getStringWidth(this.getRecordPlaying(gui)) / 2, -4, l + (l1 << 24 & -16777216));
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}

			mc.mcProfiler.endSection();
		}
	}

	private GuiSpectator getSpectatorGui(GuiIngame gui) {
		return (GuiSpectator) Reflection.getValue(GuiIngame.class, gui, "spectatorGui");
	}

	private void func_181551_a(GuiIngame gui, ScaledResolution res) {
		Reflection.call(GuiIngame.class, gui, "func_181551_a", "(Lnet/minecraft/client/gui/ScaledResolution;)V", res);
	}
	
	private GuiOverlayDebug getOverlayDebug(GuiIngame gui) {
		return (GuiOverlayDebug) Reflection.getValue(GuiIngame.class, gui, "overlayDebug");
	}
	
	private int getRecordPlayingUpFor(GuiIngame gui) {
		return (int) Reflection.getValue(GuiIngame.class, gui, "recordPlayingUpFor");
	}
	
	private boolean getRecordIsPlaying(GuiIngame gui) {
		return (boolean) Reflection.getValue(GuiIngame.class, gui, "recordIsPlaying");
	}
	
	private String getRecordPlaying(GuiIngame gui) {
		return (String) Reflection.getValue(GuiIngame.class, gui, "recordPlaying");
	}
}
