package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

public interface SaveGameListener extends Listener {

	public void onSaveGame(SaveGameEvent event);
	
	public static class SaveGameEvent extends Event {
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof SaveGameListener)
					((SaveGameListener) l).onSaveGame(this);
			}
		}
	}
}
