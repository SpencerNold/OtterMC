package io.github.ottermc.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.AttackEntityListener;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Transformer(className = "net/minecraft/client/multiplayer/PlayerControllerMP")
public class PlayerControllerMPTransformer {

	@Injector(target = Target.HEAD, name = "attackEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)V")
	public void onAttackEntity(PlayerControllerMP controller, EntityPlayer player, Entity entity, Callback callback) {
		AttackEntityListener.AttackEntityEvent event = new AttackEntityListener.AttackEntityEvent(controller, player, entity);
		EventBus.fire(event);
	}
}
