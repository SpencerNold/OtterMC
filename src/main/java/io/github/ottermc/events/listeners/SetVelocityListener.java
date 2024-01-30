package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.entity.Entity;

public interface SetVelocityListener extends Listener {

	public void onSetVelocity(SetVelocityEvent event);
	
	// analytical data; unused by the client
	public static class SetVelocityEvent extends Event {
		
		private final Entity entity;
		private final double x, y, z;
		
		public SetVelocityEvent(Entity entity, double x, double y, double z) {
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Entity getEntity() {
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
