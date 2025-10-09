package io.github.ottermc.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

public interface SetEntityDamageBrightnessListener extends Listener {

	void onSetEntityDamageBrightness(SetEntityDamageBrightnessEvent event);
	
	class SetEntityDamageBrightnessEvent extends Event {
		
		private float red, green, blue, alpha;
		
		public SetEntityDamageBrightnessEvent() {
			this.red = 1.0f;
			this.green = 0.0f;
			this.blue = 0.0f;
			this.alpha = 1.0f;
		}
		
		public float getRed() {
			return red;
		}
		
		public void setRed(float red) {
			this.red = red;
		}
		
		public float getGreen() {
			return green;
		}
		
		public void setGreen(float green) {
			this.green = green;
		}
		
		public float getBlue() {
			return blue;
		}
		
		public void setBlue(float blue) {
			this.blue = blue;
		}
		
		public float getAlpha() {
			return alpha;
		}
		
		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof SetEntityDamageBrightnessListener)
					((SetEntityDamageBrightnessListener) l).onSetEntityDamageBrightness(this);
			}
		}
	}
}
