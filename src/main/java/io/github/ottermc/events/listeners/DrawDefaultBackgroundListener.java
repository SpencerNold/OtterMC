package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.gui.GuiScreen;

public interface DrawDefaultBackgroundListener extends Listener {

	public void onDrawDefaultBackground(DrawDefaultBackgroundEvent event);
	
	public static class DrawDefaultBackgroundEvent extends Event {
		
		private final GuiScreen gui;
		private boolean canceled;
		
		public DrawDefaultBackgroundEvent(GuiScreen gui) {
			this.gui = gui;
		}
		
		public GuiScreen getGui() {
			return gui;
		}
		
		public boolean isCanceled() {
			return canceled;
		}
		
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof DrawDefaultBackgroundListener)
					((DrawDefaultBackgroundListener) l).onDrawDefaultBackground(this);
			}
		}
	}
}
