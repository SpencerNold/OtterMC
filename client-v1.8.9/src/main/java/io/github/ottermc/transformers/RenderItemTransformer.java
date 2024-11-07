package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.transformers.wrapper.RenderItemWrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

@Transformer(name = "net/minecraft/client/renderer/entity/RenderItem")
public class RenderItemTransformer {

	@Injector(target = Target.HEAD, name = "renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V")
	public void onRenderModel(RenderItem renderer, IBakedModel model, Callback callback) {
		RenderItemWrapper.renderEffect(renderer, model);
		callback.setCanceled(true);
	}
	
	@Injector(target = Target.HEAD, name = "renderItem(Lnet/minecraft/item/ItemStack;net/minecraft/client/resources/model/IBakedModel)V")
	public void onRenderItem(RenderItem renderer, ItemStack itemStack, IBakedModel model, Callback callback) {
		GlStateManager.scale(2.0, 2.0, 2.0);
	}
}
