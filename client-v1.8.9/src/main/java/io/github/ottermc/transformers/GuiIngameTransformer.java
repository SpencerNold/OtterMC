package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderGameOverlayListener.RenderGameOverlayEvent;
import net.minecraft.client.gui.GuiIngame;

@Transformer(name = "net/minecraft/client/gui/GuiIngame")
public class GuiIngameTransformer {

	@Injector(target = Target.HEAD, name = "renderGameOverlay(F)V")
	public void onRenderGameOverlay(GuiIngame guiIngame, float partialTicks, Callback callback) {
		RenderGameOverlayEvent event = new RenderGameOverlayEvent(guiIngame, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
