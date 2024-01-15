package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderWorldListener.RenderWorldEvent;
import net.minecraft.client.renderer.EntityRenderer;

@Transformer(className = "net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererTransformer {

	@Injector(target = Target.TAIL, name = "renderWorld(FJ)V")
	public void onRenderWorld(EntityRenderer renderer, float partialTicks, long finishTimeNano, Callback callback) {
		RenderWorldEvent event = new RenderWorldEvent(partialTicks, finishTimeNano);
		EventBus.fire(event);
	}
}
