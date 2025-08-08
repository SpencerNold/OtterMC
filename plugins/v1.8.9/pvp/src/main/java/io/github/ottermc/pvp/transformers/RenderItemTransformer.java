package io.github.ottermc.pvp.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.pvp.transformers.wrapper.RenderItemWrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

@Transformer(className = "net/minecraft/client/renderer/entity/RenderItem")
public class RenderItemTransformer {

	@Injector(target = Target.HEAD, name = "renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V")
	public void onRenderModel(RenderItem renderer, IBakedModel model, Callback callback) {
		RenderItemWrapper.renderEffect(renderer, model);
		callback.setCanceled(true);
	}
	
	@Injector(target = Target.HEAD, name = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V")
	public void onRenderItem(RenderItem renderer, ItemStack itemStack, IBakedModel model, Callback callback) {
		GlStateManager.scale(2.0, 2.0, 2.0);
	}
}
