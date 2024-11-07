package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.Minecraft;

public interface RunTickListener extends Listener {

	public void onRunTick(RunTickEvent event);
	
	public static class RunTickEvent extends Event {
		
		private final Minecraft minecraft;
		
		public RunTickEvent(Minecraft minecraft) {
			this.minecraft = minecraft;
		}
		
		public Minecraft getMinecraft() {
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
