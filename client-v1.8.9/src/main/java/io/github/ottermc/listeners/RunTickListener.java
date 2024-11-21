package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RunTickListener extends Listener {

	void onRunTick(RunTickEvent event);
	
	class RunTickEvent extends Event {
		
		private final Object minecraft;
		
		public RunTickEvent(Object minecraft) {
			this.minecraft = minecraft;
		}
		
		public Object getMinecraft() {
			return minecraft;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				Listener listener = listeners.get(i);
				if (listener instanceof RunTickListener)
					((RunTickListener) listener).onRunTick(this);
			}
		}
	}
}
