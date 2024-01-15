package io.github.ottermc.screen.hud.minecraft;

import agent.Reflection;
import io.github.ottermc.screen.hud.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.world.World;

public class MinecraftTabHud extends Component {
	
	public MinecraftTabHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		World world = mc.theWorld;
		net.minecraft.scoreboard.Scoreboard scoreboard = world.getScoreboard();
        ScoreObjective scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (!mc.gameSettings.keyBindPlayerList.isKeyDown() || mc.isIntegratedServerRunning() && mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null)
        {
            this.getOverlayPlayerList(gui).updatePlayerList(false);
        }
        else
        {
            this.getOverlayPlayerList(gui).updatePlayerList(true);
            this.getOverlayPlayerList(gui).renderPlayerlist(res.getScaledWidth(), scoreboard, scoreobjective1);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();		
	}

	private GuiPlayerTabOverlay getOverlayPlayerList(GuiIngame gui) {
		return (GuiPlayerTabOverlay) Reflection.getMinecraftField("net/minecraft/client/gui/GuiIngame", "overlayPlayerList", gui);
	}
}
