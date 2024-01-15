package io.github.ottermc.screen.hud.client;

import java.util.ArrayList;

import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class ClientPotionEffectHud extends Component implements Movable {

	private static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

	public ClientPotionEffectHud() {
		super(false);
		x = 4;
		y = -1;
	}

	@Override
	protected void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		if (mc.getRenderViewEntity() instanceof EntityPlayer) {
			EntityLivingBase player = (EntityLivingBase) mc.getRenderViewEntity();
			ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>(player.getActivePotionEffects());
			height = effects.size() * 20;
			int color = io.github.ottermc.modules.hud.PotionEffect.getColor().getValue();
			for (int i = 0; i < effects.size(); i++) {
				PotionEffect effect = effects.get(i);
				if (effect == null)
					continue;
				Potion potion = Potion.potionTypes[effect.getPotionID()];
				int index = potion.getStatusIconIndex();
				mc.getTextureManager().bindTexture(inventoryBackground);
				drawable.drawTexturedModalRect(getX() + 4, getY() + (i * 20) + 4, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
				String text = getPotionDisplayString(effect);
				width = Math.max(width, mc.fontRendererObj.getStringWidth(text) + 32);
				mc.fontRendererObj.drawString(text, getX() + 28, getY() + (i * 20) + 9, color, false);
			}
		}
	}

	private String getPotionDisplayString(PotionEffect effect) {
		String name = I18n.format(effect.getEffectName(), new Object[0]);
		if (effect.getAmplifier() == 1)
			name = name + " " + I18n.format("enchantment.level.2", new Object[0]);
		else if (effect.getAmplifier() == 2)
			name = name + " " + I18n.format("enchantment.level.3", new Object[0]);
		else if (effect.getAmplifier() == 3)
			name = name + " " + I18n.format("enchantment.level.4", new Object[0]);
		String time = Potion.getDurationString(effect);
		return name + " " + time;
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
		return x;
	}

	@Override
	public int getY() {
		return y == -1 ? y = (int) drawable.getHeight() - getRawHeight() - 4 : y;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
}
