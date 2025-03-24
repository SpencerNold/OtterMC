package io.github.ottermc.pvp.transformers.wrapper;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.RenderEffectListener;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.ResourceLocation;

public class RenderItemWrapper {

	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	
	public static void renderEffect(RenderItem renderer, IBakedModel model) {
		Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        
        RenderEffectListener.RenderEffectEvent event = new RenderEffectListener.RenderEffectEvent(model, -8372020);
        EventBus.fire(event);
        int color = event.getColor();
        
        renderModel(renderer, model, color);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        renderModel(renderer, model, color);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	}

	private static void renderModel(RenderItem renderer, IBakedModel model, int color) {
            Reflection.call(RenderItem.class, renderer, "renderModel", "(Lnet/minecraft/client/resources/model/IBakedModel;I)V", model, color);
	}
}
