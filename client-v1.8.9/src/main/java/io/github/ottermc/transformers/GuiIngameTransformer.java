package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.RenderGameOverlayListener.RenderGameOverlayEvent;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
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
