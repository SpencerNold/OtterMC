package io.github.ottermc.pvp.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface AddChatMessageListener extends Listener {

	void onAddChatMessage(AddChatMessageEvent event);
	
	class AddChatMessageEvent extends Event {
		
		private String message;

		public AddChatMessageEvent(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof AddChatMessageListener)
					((AddChatMessageListener) l).onAddChatMessage(this);
			}
		}
	}
}
