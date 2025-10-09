package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface AttackEntityListener extends Listener {

	void onAttackEntity(AttackEntityEvent event);
	
	// analytical data; unused by the client
	class AttackEntityEvent extends Event {

		private final Object controller;
		private final Object player;
		private final Object entity;
		
		public AttackEntityEvent(Object controller, Object player, Object entity) {
			this.controller = controller;
			this.player = player;
			this.entity = entity;
		}
		
		public Object getController() {
			return controller;
		}
		
		public Object getPlayer() {
			return player;
		}
		
		public Object getEntity() {
			return entity;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof AttackEntityListener)
					((AttackEntityListener) l).onAttackEntity(this);
			}
		}
	}
}
