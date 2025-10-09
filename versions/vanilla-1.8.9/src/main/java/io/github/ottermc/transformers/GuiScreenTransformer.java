package io.github.ottermc.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.DrawDefaultBackgroundListener;
import net.minecraft.client.gui.GuiScreen;

@Transformer(className = "net/minecraft/client/gui/GuiScreen")
public class GuiScreenTransformer {

	@Injector(target = Target.HEAD, name = "drawDefaultBackground()V")
	public void onDrawDefaultBackground(GuiScreen gui, Callback callback) {
		DrawDefaultBackgroundListener.DrawDefaultBackgroundEvent event = new DrawDefaultBackgroundListener.DrawDefaultBackgroundEvent(gui);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
