package io.github.ottermc.pvp.screen.hud.client;

import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ClientArmorStatusHud extends Component implements Movable {
	
	private static final ResourceLocation EMPTY_HELMET = new ResourceLocation("textures/items/empty_armor_slot_helmet.png");
	private static final ResourceLocation EMPTY_CHESTPLATE = new ResourceLocation("textures/items/empty_armor_slot_chestplate.png");
	private static final ResourceLocation EMPTY_LEGGINGS = new ResourceLocation("textures/items/empty_armor_slot_leggings.png");
	private static final ResourceLocation EMPTY_BOOTS = new ResourceLocation("textures/items/empty_armor_slot_boots.png");
	
	public ClientArmorStatusHud() {
		super(false);
		x = -1;
		y = -1;
		width = 16;
		height = 64;
	}

	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (!Display.isFullscreen())
			return;
		if (mc.getRenderViewEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			for (int i = 0; i < 4; i++) {
				ItemStack stack = player.inventory.armorItemInSlot(3 - i);
				if (stack == null) {
					switch (i) {
					case 0:
						mc.getTextureManager().bindTexture(EMPTY_HELMET);
						break;
					case 1:
						mc.getTextureManager().bindTexture(EMPTY_CHESTPLATE);
						break;
					case 2:
						mc.getTextureManager().bindTexture(EMPTY_LEGGINGS);
						break;
					case 3:
						mc.getTextureManager().bindTexture(EMPTY_BOOTS);
						break;
					}
					Gui.drawModalRectWithCustomSizedTexture(getX(), getY() + (i * 16), 0, 0, 16, 16, 16, 16);
					continue;
				}
				RenderItem renderer = mc.getRenderItem();
				renderer.renderItemAndEffectIntoGUI(stack, getX(), getY() + (i * 16));
				renderer.renderItemOverlays(mc.fontRendererObj, stack, getX(), getY() + (i * 16));
			}
			GlStateManager.disableLighting();
		}
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
	public int getX() {
		return x == -1 ? x = 2 : x;
	}

	@Override
	public int getY() {
		return y == -1 ? (y = drawable.middle((int) drawable.getHeight(), getRawHeight()) - 32) : y;
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
	public int getSerialId() {
		return "ARMOR_STATUS_COMPONENT".hashCode();
	}
}
