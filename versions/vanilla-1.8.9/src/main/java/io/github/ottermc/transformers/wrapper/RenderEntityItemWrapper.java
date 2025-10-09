package io.github.ottermc.transformers.wrapper;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.GetItemScaleListener;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class RenderEntityItemWrapper {

	private static final String renderEntityItemClassName = "net/minecraft/client/renderer/entity/RenderEntityItem";
	private static final String renderClassName = "net/minecraft/client/renderer/entity/Render";
	
	public static void doRender(RenderEntityItem renderer, EntityItem entity, double x, double y, double z, float yaw, float partialTicks) {
		ItemStack itemstack = entity.getEntityItem();
		Random random = getField_177079_e(renderer);
		random.setSeed(187L);
		boolean flag = false;
		if (bindEntityTexture(renderer, entity)) {
			getRenderManager().renderEngine.getTexture(getEntityTexture(renderer, entity)).setBlurMipmap(false, false);
			flag = true;
		}
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = getItemRenderer().getItemModelMesher().getItemModel(itemstack);
		int i = func_177077_a(renderer, entity, x, y, z, partialTicks, ibakedmodel);
		for (int j = 0; j < i; ++j) {
			if (ibakedmodel.isGui3d()) {
				GlStateManager.pushMatrix();
				if (j > 0) {
					float f = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f1 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f2 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					GlStateManager.translate(f, f1, f2);
				}
				GetItemScaleListener.GetItemScaleEvent event = new GetItemScaleListener.GetItemScaleEvent(renderer, entity, 0.5f, 0.5f, 0.5f);
				EventBus.fire(event);
				GlStateManager.scale(event.getX(), event.getY(), event.getZ());
				ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
				getItemRenderer().renderItem(itemstack, ibakedmodel);
				GlStateManager.popMatrix();
			} else {
				GlStateManager.pushMatrix();
				GetItemScaleListener.GetItemScaleEvent event = new GetItemScaleListener.GetItemScaleEvent(renderer, entity, 1.0f, 1.0f, 1.0f);
				EventBus.fire(event);
				GlStateManager.scale(event.getX(), event.getY(), event.getZ());
				ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
				getItemRenderer().renderItem(itemstack, ibakedmodel);
				GlStateManager.popMatrix();
				float f3 = ibakedmodel.getItemCameraTransforms().ground.scale.x;
				float f4 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
				float f5 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
				GlStateManager.translate(0.0F * f3, 0.0F * f4, 0.046875F * f5);
			}
		}

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		bindEntityTexture(renderer, entity);

		if (flag) {
			getRenderManager().renderEngine.getTexture(getEntityTexture(renderer, entity)).restoreLastBlurMipmap();
		}
	}
	
	private static Random getField_177079_e(RenderEntityItem renderer) {
		return (Random) Reflection.getValue(renderEntityItemClassName, renderer, "field_177079_e");
	}
	
	private static boolean bindEntityTexture(RenderEntityItem renderer, Entity entity) {
		return (boolean) Reflection.call(renderClassName, renderer, "bindEntityTexture", "(Lnet/minecraft/entity/Entity;)Z", entity);
	}
	
	private static RenderManager getRenderManager() {
		return Minecraft.getMinecraft().getRenderManager();
	}
	
	private static RenderItem getItemRenderer() {
		return Minecraft.getMinecraft().getRenderItem();
	}
	
	private static ResourceLocation getEntityTexture(RenderEntityItem renderer, Entity entity) {
		return TextureMap.locationBlocksTexture; // Should be fine? If not, replace this with reflection
	}
	
	private static int func_177077_a(RenderEntityItem renderer, EntityItem entity, double x, double y, double z, float partialTicks, IBakedModel model) {
		return (int) Reflection.call(renderEntityItemClassName, renderer, "func_177077_a", "(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I", entity, x, y, z, partialTicks, model);
	}
}
