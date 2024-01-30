package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.DrawDefaultBackgroundListener.DrawDefaultBackgroundEvent;
import net.minecraft.client.gui.GuiScreen;

@Transformer(name = "net/minecraft/client/gui/GuiScreen")
public class GuiScreenTransformer {

	@Injector(target = Target.HEAD, name = "drawDefaultBackground()V")
	public void onDrawDefaultBackground(GuiScreen gui, Callback callback) {
		DrawDefaultBackgroundEvent event = new DrawDefaultBackgroundEvent(gui);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
