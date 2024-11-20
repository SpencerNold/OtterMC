package io.github.ottermc.pvp.screen.hud.minecraft;

import io.github.ottermc.screen.hud.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MinecraftSleepMenuHud extends Component {

	public MinecraftSleepMenuHud() {
		super(true);
	}
	
	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		Entity player = mc.thePlayer;
		
		GlStateManager.disableBlend();
        if (((EntityPlayer) player).getSleepTimer() > 0)
        {
            mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int j1 = ((EntityPlayer) player).getSleepTimer();
            float f1 = (float)j1 / 100.0F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F - (float)(j1 - 100) / 10.0F;
            }

            int k = (int)(220.0F * f1) << 24 | 1052704;
	        int i = res.getScaledWidth();
	        int j = res.getScaledHeight();
            Gui.drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            mc.mcProfiler.endSection();
        }
	}
}
