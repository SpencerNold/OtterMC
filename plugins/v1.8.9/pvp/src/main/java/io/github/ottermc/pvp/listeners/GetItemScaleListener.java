package io.github.ottermc.pvp.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface GetItemScaleListener extends Listener {

	void onGetItemScale(GetItemScaleEvent event);
	
	class GetItemScaleEvent extends Event {
		
		private final Object renderItemParentObject;
		private final Object entityItemParameter;
		
		private float x, y, z;
		
		public GetItemScaleEvent(Object renderItemParentObject, Object entityItemParameter, float x, float y, float z) {
			this.renderItemParentObject = renderItemParentObject;
			this.entityItemParameter = entityItemParameter;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Object getRenderItemParentObject() {
			return renderItemParentObject;
		}
		
		public Object getEntityItemParameter() {
			return entityItemParameter;
		}
		
		public float getX() {
			return x;
		}
		
		public float getY() {
			return y;
		}
		
		public float getZ() {
			return z;
		}
		
		public void setScale(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof GetItemScaleListener)
					((GetItemScaleListener) l).onGetItemScale(this);
			}
		}
	}
}
