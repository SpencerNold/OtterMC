package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderGameOverlayListener.RenderGameOverlayEvent;
import net.minecraft.client.gui.GuiIngame;

@Transformer(className = "net/minecraft/client/gui/GuiIngame")
public class GuiIngameTransformer {

	@Injector(target = Target.HEAD, name = "renderGameOverlay(F)V")
	public void onRenderGameOverlay(GuiIngame guiIngame, float partialTicks, Callback callback) {
		RenderGameOverlayEvent event = new RenderGameOverlayEvent(guiIngame, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}