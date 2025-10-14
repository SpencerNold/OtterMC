package io.github.ottermc.universal.hud;

import io.github.ottermc.render.hud.impl.PotionEffectHud;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.universal.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientPotionEffectHud extends PotionEffectHud {

    private static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

    @Override
    public void draw(@Type(DrawableHelper.class) Object context) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityLivingBase player = (EntityLivingBase) mc.getRenderViewEntity();
            ArrayList<PotionEffect> effects = new ArrayList<>(player.getActivePotionEffects());
            ScaledResolution resolution = new ScaledResolution(mc);
            drawEffects(mc, (DrawableHelper) context, effects, (getYOffset() - getDefaultY()) > (resolution.getScaledHeight() / 2));
        }
    }

    @Override
    public void drawDummyObject(@Type(DrawableHelper.class) Object context) {
        Minecraft mc = Minecraft.getMinecraft();
        drawEffects(mc, (DrawableHelper) context, Arrays.asList(
                new PotionEffect(1, 3600, 2),
                new PotionEffect(5, 3600, 2),
                new PotionEffect(12, 3600)
        ), false);
    }

    private void drawEffects(Minecraft mc, DrawableHelper drawable, List<PotionEffect> effects, boolean drawBottomUp) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();

        int color = io.github.ottermc.modules.impl.hud.PotionEffect.getColor().getValue();
        for (int i = 0; i < effects.size(); i++) {
            PotionEffect effect = effects.get(i);
            if (effect == null)
                continue;
            int x = getDefaultX();
            int y = getDefaultY() + (drawBottomUp ? (getRawHeight() + ((i + 1) * -20)) : (i * 20));
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            int index = potion.getStatusIconIndex();
            mc.getTextureManager().bindTexture(inventoryBackground);
            drawable.drawTexturedModalRect(x, y, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
            String text = getPotionDisplayString(effect);
            mc.fontRendererObj.drawString(text, x + 20, y + 5.5f, color, false);
        }
    }

    private String getPotionDisplayString(PotionEffect effect) {
        String time = Potion.getDurationString(effect);
        String name = time + " " + I18n.format(effect.getEffectName(), new Object[0]);
        if (effect.getAmplifier() == 1)
            name = name + " " + I18n.format("enchantment.level.2", new Object[0]);
        else if (effect.getAmplifier() == 2)
            name = name + " " + I18n.format("enchantment.level.3", new Object[0]);
        else if (effect.getAmplifier() == 3)
            name = name + " " + I18n.format("enchantment.level.4", new Object[0]);
        return name;
    }
}
