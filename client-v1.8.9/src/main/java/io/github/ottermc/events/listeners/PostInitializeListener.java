package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

public interface PostInitializeListener extends Listener {

	public void onPostInitializeListener(PostInitializeEvent event);
	
	public static class PostInitializeEvent extends Event {
		@Override
		public void fire(List<Listener> listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				Listener l = listeners.get(i);
				if (l instanceof PostInitializeListener)
					((PostInitializeListener) l).onPostInitializeListener(this);
			}
		}
	}
}
