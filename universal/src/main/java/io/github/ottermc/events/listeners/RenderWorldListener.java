package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

public interface RenderWorldListener extends Listener {

	void onRenderWorld(RenderWorldEvent event);
	
	class RenderWorldEvent extends Event {

		private final float partialTicks;
		private final long finishTimeNano;
		
		public RenderWorldEvent(float partialTicks, long finishTimeNano) {
			this.partialTicks = partialTicks;
			this.finishTimeNano = finishTimeNano;
		}
		
		public float getPartialTicks() {
			return partialTicks;
		}
		
		public long getFinishTimeNano() {
			return finishTimeNano;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener listener : listeners) {
				if (listener instanceof RenderWorldListener)
					((RenderWorldListener) listener).onRenderWorld(this);
			}
		}
	}
}
