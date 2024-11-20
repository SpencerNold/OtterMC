package io.github.ottermc.pvp.screen.hud.minecraft;

import agent.Reflection;
import io.github.ottermc.screen.hud.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MinecraftPumpkinOverlayHud extends Component {
	
	public MinecraftPumpkinOverlayHud() {
		super(true);
	}

	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		ItemStack itemstack = ((EntityPlayer) mc.thePlayer).inventory.armorItemInSlot(3);
		if (mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
			this.renderPumpkinOverlay(gui, res);
		}
	}
	
	private void renderPumpkinOverlay(GuiIngame gui, ScaledResolution res) {
		Reflection.invokeMinecraft("net/minecraft/client/gui/GuiIngame", "renderPumpkinOverlay(Lnet/minecraft/client/gui/ScaledResolution;)V", gui, res);
	}
}
