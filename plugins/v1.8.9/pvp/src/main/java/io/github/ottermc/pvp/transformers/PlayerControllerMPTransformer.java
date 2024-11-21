package io.github.ottermc.pvp.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.AttackEntityListener;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Transformer(name = "net/minecraft/client/multiplayer/PlayerControllerMP")
public class PlayerControllerMPTransformer {

	@Injector(target = Target.HEAD, name = "attackEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)V")
	public void onAttackEntity(PlayerControllerMP controller, EntityPlayer player, Entity entity, Callback callback) {
		AttackEntityListener.AttackEntityEvent event = new AttackEntityListener.AttackEntityEvent(controller, player, entity);
		EventBus.fire(event);
	}
}
