package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.gui.GuiIngame;

public interface RenderGameOverlayListener extends Listener {

	public void onRenderGameOverlay(RenderGameOverlayEvent event);
	
	public static class RenderGameOverlayEvent extends Event {

		private final GuiIngame gui;
		private final float partialTicks;
		private boolean canceled;
		
		public RenderGameOverlayEvent(GuiIngame gui, float partialTicks) {
			this.gui = gui;
			this.partialTicks = partialTicks;
		}

		public GuiIngame getGuiIngame() {
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
