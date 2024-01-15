package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

public interface AddChatMessageListener extends Listener {

	public void onAddChatMessage(AddChatMessageEvent event);
	
	public static class AddChatMessageEvent extends Event {
		
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
