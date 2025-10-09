package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface SetVelocityListener extends Listener {

	void onSetVelocity(SetVelocityEvent event);
	
	// analytical data; unused by the client
	class SetVelocityEvent extends Event {
		
		private final Object entity;
		private final double x, y, z;
		
		public SetVelocityEvent(Object entity, double x, double y, double z) {
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Object getEntity() {
			return entity;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
		
		public double getZ() {
			return z;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof SetVelocityListener)
					((SetVelocityListener) l).onSetVelocity(this);
			}
		}
	}
}
