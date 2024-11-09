package io.github.ottermc.events.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RenderEffectListener extends Listener {

	void onRenderEffect(RenderEffectEvent event);
	
	class RenderEffectEvent extends Event {
		
		private final Object model;
		private int color;
		
		public RenderEffectEvent(Object model, int color) {
			this.model = model;
			this.color = color;
		}
		
		public Object getModel() {
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
