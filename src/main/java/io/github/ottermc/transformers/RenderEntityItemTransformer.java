package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.transformers.wrapper.RenderEntityItemWrapper;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;

@Transformer(name = "net/minecraft/client/renderer/entity/RenderEntityItem")
public class RenderEntityItemTransformer {
	
	@Injector(target = Target.HEAD, name = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V")
	public void onDoRender(RenderEntityItem renderer, EntityItem entity, double x, double y, double z, float yaw, float partialTicks, Callback callback) {
		RenderEntityItemWrapper.doRender(renderer, entity, x, y, z, yaw, partialTicks);
		callback.setCanceled(true);
	}
}
