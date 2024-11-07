package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.resources.model.IBakedModel;

public interface RenderEffectListener extends Listener {

	public void onRenderEffect(RenderEffectEvent event);
	
	public static class RenderEffectEvent extends Event {
		
		private final IBakedModel model;
		private int color;
		
		public RenderEffectEvent(IBakedModel model, int color) {
			this.model = model;
			this.color = color;
		}
		
		public IBakedModel getModel() {
			return model;
		}
		
		public int getColor() {
			return color;
		}
		
		public void setColor(int color) {
			this.color = color;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener listener : listeners) {
				if (listener instanceof RenderEffectListener)
					((RenderEffectListener) listener).onRenderEffect(this);
			}
		}
	}
}
