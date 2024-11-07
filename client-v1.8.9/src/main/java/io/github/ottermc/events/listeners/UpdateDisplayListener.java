package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

public interface UpdateDisplayListener extends Listener {

	public void onUpdateDisplay(UpdateDisplayEvent event);
	
	public static class UpdateDisplayEvent extends Event {
		@Override
		public void fire(List<Listener> listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				Listener listener = listeners.get(i);
				if (listener instanceof UpdateDisplayListener)
					((UpdateDisplayListener) listener).onUpdateDisplay(this);
			}
		}
	}
}
