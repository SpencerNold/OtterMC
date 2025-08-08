package io.github.ottermc.screen.hud.minecraft;

import agent.adapters.MinecraftClassNameAdapter;
import io.github.ottermc.screen.hud.Component;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class MinecraftPlayerStatsHud extends Component {

	public MinecraftPlayerStatsHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (mc.playerController.shouldDrawHUD()) {
			boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.renderPlayerStats(gui, res);
			if (alpha)
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			else
				GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
	}
	
	private void renderPlayerStats(GuiIngame gui, ScaledResolution res) {
		Reflection.call("net/minecraft/client/gui/GuiIngame", gui, "renderPlayerStats", "(Lnet/minecraft/client/gui/ScaledResolution;)V", res);
	}
}
