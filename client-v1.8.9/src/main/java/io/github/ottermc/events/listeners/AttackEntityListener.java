package io.github.ottermc.events.listeners;

import java.util.List;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public interface AttackEntityListener extends Listener {

	public void onAttackEntity(AttackEntityEvent event);
	
	// analytical data; unused by the client
	public static class AttackEntityEvent extends Event {

		private final PlayerControllerMP controller;
		private final EntityPlayer player;
		private final Entity entity;
		
		public AttackEntityEvent(PlayerControllerMP controller, EntityPlayer player, Entity entity) {
			this.controller = controller;
			this.player = player;
			this.entity = entity;
		}
		
		public PlayerControllerMP getController() {
			return controller;
		}
		
		public EntityPlayer getPlayer() {
			return player;
		}
		
		public Entity getEntity() {
			return entity;
		}
		
		@Override
		public void fire(List<Listener> listeners) {
			for (Listener l : listeners) {
				if (l instanceof AttackEntityListener)
					((AttackEntityListener) l).onAttackEntity(this);
			}
		}
	}
}
