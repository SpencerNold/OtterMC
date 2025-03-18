package io.github.ottermc.pvp.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.RenderItemInFirstPersonListener;
import net.minecraft.client.renderer.ItemRenderer;

@Transformer(className = "net/minecraft/client/renderer/ItemRenderer")
public class ItemRendererTransformer {

	@Injector(target = Target.HEAD, name = "renderItemInFirstPerson(F)V")
	public void onRenderItemInFirstPerson(ItemRenderer renderer, float partialTicks, Callback callback) {
		RenderItemInFirstPersonListener.RenderItemInFirstPersonEvent event = new RenderItemInFirstPersonListener.RenderItemInFirstPersonEvent(renderer, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
