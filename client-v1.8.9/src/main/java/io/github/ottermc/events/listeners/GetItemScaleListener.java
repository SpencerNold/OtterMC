package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;

public interface GetItemScaleListener extends Listener {

	public void onGetItemScale(GetItemScaleEvent event);
	
	public static class GetItemScaleEvent extends Event {
		
		private final RenderEntityItem renderItemParentObject;
		private final EntityItem entityItemParameter;
		
		private float x, y, z;
		
		public GetItemScaleEvent(RenderEntityItem renderItemParentObject, EntityItem entityItemParameter, float x, float y, float z) {
			this.renderItemParentObject = renderItemParentObject;
			this.entityItemParameter = entityItemParameter;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public RenderEntityItem getRenderItemParentObject() {
			return renderItemParentObject;
		}
		
		public EntityItem getEntityItemParameter() {
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
