package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RenderItemInFirstPersonListener extends Listener {

	void onRenderItemInFirstPerson(RenderItemInFirstPersonEvent event);
	
	class RenderItemInFirstPersonEvent extends Event {
		
		private final Object renderer;
		private final float partialTicks;
		private boolean canceled;
		
		public RenderItemInFirstPersonEvent(Object renderer, float partialTicks) {
			this.renderer = renderer;
			this.partialTicks = partialTicks;
		}
		
		public boolean isCanceled() {
			return canceled;
		}
		
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
		
		public Object getRenderer() {
			return renderer;
		}
		
		public float getPartialTicks() {
			return partialTicks;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof RenderItemInFirstPersonListener)
					((RenderItemInFirstPersonListener) l).onRenderItemInFirstPerson(this);
			}
		}
	}
}
