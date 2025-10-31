package io.github.ottermc.events.listeners;

import io.github.ottermc.events.CancelableEvent;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RenderGameOverlayListener extends Listener {

	void onRenderGameOverlay(RenderGameOverlayEvent event);
	
	class RenderGameOverlayEvent extends CancelableEvent {

		private final Object context;
		private final float partialTicks;
		public RenderGameOverlayEvent(Object context, float partialTicks) {
			this.context = context;
			this.partialTicks = partialTicks;
		}

		public Object getContext() {
			return context;
		}
		
		public float getPartialTicks() {
			return partialTicks;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener listener : listeners) {
				if (listener instanceof RenderGameOverlayListener)
					((RenderGameOverlayListener) listener).onRenderGameOverlay(this);
			}
		}
	}
}
