package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.transformers.wrapper.RenderItemWrapper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;

@Transformer(className = "net/minecraft/client/renderer/entity/RenderItem")
public class RenderItemTransformer {

	@Injector(target = Target.HEAD, name = "renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V")
	public void onRenderModel(RenderItem renderer, IBakedModel model, Callback callback) {
		RenderItemWrapper.renderEffect(renderer, model);
		callback.setCanceled(true);
	}
}
