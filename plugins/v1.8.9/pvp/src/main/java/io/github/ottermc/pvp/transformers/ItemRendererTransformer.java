package io.github.ottermc.pvp.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.RenderItemInFirstPersonListener;
import net.minecraft.client.renderer.ItemRenderer;

@Transformer(name = "net/minecraft/client/renderer/ItemRenderer")
public class ItemRendererTransformer {

	@Injector(target = Target.HEAD, name = "renderItemInFirstPerson(F)V")
	public void onRenderItemInFirstPerson(ItemRenderer renderer, float partialTicks, Callback callback) {
		RenderItemInFirstPersonListener.RenderItemInFirstPersonEvent event = new RenderItemInFirstPersonListener.RenderItemInFirstPersonEvent(renderer, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
