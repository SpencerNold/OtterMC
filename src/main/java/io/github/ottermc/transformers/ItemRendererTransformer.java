package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderItemInFirstPersonListener.RenderItemInFirstPersonEvent;
import net.minecraft.client.renderer.ItemRenderer;

@Transformer(className = "net/minecraft/client/renderer/ItemRenderer")
public class ItemRendererTransformer {

	@Injector(target = Target.HEAD, name = "renderItemInFirstPerson(F)V")
	public void onRenderItemInFirstPerson(ItemRenderer renderer, float partialTicks, Callback callback) {
		RenderItemInFirstPersonEvent event = new RenderItemInFirstPersonEvent(renderer, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
