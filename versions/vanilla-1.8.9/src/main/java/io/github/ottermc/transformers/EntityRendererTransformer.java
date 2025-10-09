package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.RenderWorldListener.RenderWorldEvent;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.renderer.EntityRenderer;

@Transformer(className = "net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererTransformer {

	@Injector(target = Target.TAIL, name = "renderWorld(FJ)V")
	public void onRenderWorld(EntityRenderer renderer, float partialTicks, long finishTimeNano, Callback callback) {
		RenderWorldEvent event = new RenderWorldEvent(partialTicks, finishTimeNano);
		EventBus.fire(event);
	}
}
