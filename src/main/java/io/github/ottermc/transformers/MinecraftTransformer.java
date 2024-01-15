package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.PostInitializeListener.PostInitializeEvent;
import io.github.ottermc.events.listeners.RunTickListener.RunTickEvent;
import io.github.ottermc.events.listeners.UpdateDisplayListener.UpdateDisplayEvent;
import io.github.ottermc.screen.impl.MainMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

@Transformer(className = "net/minecraft/client/Minecraft")
public class MinecraftTransformer {

	@Injector(target = Target.HEAD, name = "runTick()V")
	public void onTick(Minecraft minecraft, Callback callback) {
		RunTickEvent event = new RunTickEvent(minecraft);
		EventBus.fire(event);
	}
	
	@Injector(target = Target.HEAD, name = "updateDisplay()V")
	public void onUpdateDisplay(Minecraft minecraft, Callback callback) {
		UpdateDisplayEvent event = new UpdateDisplayEvent();
		EventBus.fire(event);
	}
	
	@Injector(target = Target.TAIL, name = "startGame()V")
	public void onStartGame(Minecraft minecraft, Callback callback) {
		PostInitializeEvent event = new PostInitializeEvent();
		EventBus.fire(event);
	}
	
	@Injector(target = Target.HEAD, name = "displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V")
	public void onDisplayGuiScreen(Minecraft mc, GuiScreen gui, Callback callback) {
		// Replace the GuiMainMenu with my own wrapped class
		if ((gui == null && mc.theWorld == null) || (gui != null && gui.getClass() == GuiMainMenu.class)) {
			mc.displayGuiScreen(new MainMenuScreen());
			callback.setCanceled(true);
		}
	}
}
