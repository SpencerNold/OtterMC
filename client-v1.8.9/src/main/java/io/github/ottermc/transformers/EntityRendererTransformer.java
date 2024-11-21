package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.RenderWorldListener.RenderWorldEvent;
import net.minecraft.client.renderer.EntityRenderer;

@Transformer(name = "net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererTransformer {

	@Injector(target = Target.TAIL, name = "renderWorld(FJ)V")
	public void onRenderWorld(EntityRenderer renderer, float partialTicks, long finishTimeNano, Callback callback) {
		RenderWorldEvent event = new RenderWorldEvent(partialTicks, finishTimeNano);
		EventBus.fire(event);
	}
}
