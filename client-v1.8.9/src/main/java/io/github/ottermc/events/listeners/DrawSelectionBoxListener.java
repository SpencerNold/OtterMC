package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public interface DrawSelectionBoxListener extends Listener {

	public void onDrawSelectionBox(DrawSelectionBoxEvent event);
	
	public static class DrawSelectionBoxEvent extends Event {
		
		private final EntityPlayer player;
		private final MovingObjectPosition target;
		private final int type;
		private final float partialTicks;
		
		private boolean canceled;
		
		public DrawSelectionBoxEvent(EntityPlayer player, MovingObjectPosition target, int type, float partialTicks) {
			this.player = player;
			this.target = target;
			this.type = type;
			this.partialTicks = partialTicks;
		}
		
		public EntityPlayer getPlayer() {
			return player;
		}
		
		public MovingObjectPosition getTarget() {
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
