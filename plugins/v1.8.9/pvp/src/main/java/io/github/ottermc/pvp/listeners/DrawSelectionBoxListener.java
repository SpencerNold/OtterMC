package io.github.ottermc.pvp.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface DrawSelectionBoxListener extends Listener {

	void onDrawSelectionBox(DrawSelectionBoxEvent event);
	
	class DrawSelectionBoxEvent extends Event {
		
		private final Object player;
		private final Object target;
		private final int type;
		private final float partialTicks;
		
		private boolean canceled;
		
		public DrawSelectionBoxEvent(Object player, Object target, int type, float partialTicks) {
			this.player = player;
			this.target = target;
			this.type = type;
			this.partialTicks = partialTicks;
		}
		
		public Object getPlayer() {
			return player;
		}
		
		public Object getTarget() {
			return target;
		}
		
		public int getType() {
			return type;
		}
		
		public float getPartialTicks() {
			return partialTicks;
		}
		
		public boolean isCanceled() {
			return canceled;
		}
		
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}

		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof DrawSelectionBoxListener)
					((DrawSelectionBoxListener) l).onDrawSelectionBox(this);
			}
		}
	}
}
