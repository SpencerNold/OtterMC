package io.github.ottermc.pvp.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import io.github.ottermc.render.Color;

public interface RenderArmorEffectListener extends Listener {

	void onRenderArmorEffect(RenderArmorEffectEvent event);
	
	class RenderArmorEffectEvent extends Event {
		
		private Color color;
		
		public RenderArmorEffectEvent(float r, float g, float b, float a) {
			this.color = new Color(r, g, b, a);
		}
		
		public void setColor(int color) {
			this.color = new Color(color, true);
		}
		
		public void setColor(Color color) {
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener listener : listeners) {
				if (listener instanceof RenderArmorEffectListener)
					((RenderArmorEffectListener) listener).onRenderArmorEffect(this);
			}
		}
	}
}
