package io.github.ottermc.transformers.wrapper;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.RenderArmorEffectListener;
import io.github.ottermc.render.Color;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerArmorBaseWrapper {

	private static final String layerArmorBaseClassName = "net/minecraft/client/renderer/entity/layers/LayerArmorBase";

	private static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	public static void func_177183_a(LayerArmorBase<ModelBase> layerArmorBase, EntityLivingBase entity, ModelBase modelBase, float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f = (float) ((Entity) entity).ticksExisted + f3;
		((Render<?>) getRenderer(layerArmorBase)).bindTexture(ENCHANTED_ITEM_GLINT_RES);
		GlStateManager.enableBlend();
		GlStateManager.depthFunc(514);
		GlStateManager.depthMask(false);
		GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0F);
		
		RenderArmorEffectListener.RenderArmorEffectEvent event = new RenderArmorEffectListener.RenderArmorEffectEvent(0.38f, 0.19f, 0.608f, 1.0f);
		EventBus.fire(event);
		Color color = event.getColor();
		
		for (int i = 0; i < 2; ++i) {
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(768, 1);
			GlStateManager.color(color.getRedNormal(), color.getGreenNormal(), color.getBlueNormal(), color.getAlphaNormal());
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
			GlStateManager.rotate(30.0F - (float) i * 60.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(0.0F, f * (0.001F + (float) i * 0.003F) * 20.0F, 0.0F);
			GlStateManager.matrixMode(5888);
			modelBase.render(entity, f1, f2, f4, f5, f6, f7);
		}
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.depthMask(true);
		GlStateManager.depthFunc(515);
		GlStateManager.disableBlend();
	}
	
	private static RendererLivingEntity<?> getRenderer(LayerArmorBase<ModelBase> layerArmorBase) {
		return (RendererLivingEntity<?>) Reflection.getValue(layerArmorBaseClassName, layerArmorBase, "renderer");
	}
}
