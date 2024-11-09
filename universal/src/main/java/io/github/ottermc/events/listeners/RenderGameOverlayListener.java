package io.github.ottermc.events.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RenderGameOverlayListener extends Listener {

	void onRenderGameOverlay(RenderGameOverlayEvent event);
	
	class RenderGameOverlayEvent extends Event {

		private final Object gui;
		private final float partialTicks;
		private boolean canceled;
		
		public RenderGameOverlayEvent(Object gui, float partialTicks) {
			this.gui = gui;
			this.partialTicks = partialTicks;
		}

		public Object getGuiIngame() {
			return gui;
		}
		
		public float getPartialTicks() {
			return partialTicks;
		}
		
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
		
		public boolean isCanceled() {
			return canceled;
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
