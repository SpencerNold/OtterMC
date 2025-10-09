package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface DrawDefaultBackgroundListener extends Listener {

	void onDrawDefaultBackground(DrawDefaultBackgroundEvent event);
	
	class DrawDefaultBackgroundEvent extends Event {
		
		private final Object gui;
		private boolean canceled;
		
		public DrawDefaultBackgroundEvent(Object gui) {
			this.gui = gui;
		}
		
		public Object getGui() {
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
