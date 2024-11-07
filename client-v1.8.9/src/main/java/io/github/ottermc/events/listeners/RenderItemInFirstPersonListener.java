package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.renderer.ItemRenderer;

public interface RenderItemInFirstPersonListener extends Listener {

	public void onRenderItemInFirstPerson(RenderItemInFirstPersonEvent event);
	
	public static class RenderItemInFirstPersonEvent extends Event {
		
		private final ItemRenderer renderer;
		private final float partialTicks;
		private boolean canceled;
		
		public RenderItemInFirstPersonEvent(ItemRenderer renderer, float partialTicks) {
			this.renderer = renderer;
			this.partialTicks = partialTicks;
		}
		
		public boolean isCanceled() {
			return canceled;
		}
		
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
		
		public ItemRenderer getRenderer() {
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
