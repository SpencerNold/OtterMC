package io.github.ottermc.events.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface PostInitializeListener extends Listener {

	void onPostInitializeListener(PostInitializeEvent event);
	
	class PostInitializeEvent extends Event {
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
