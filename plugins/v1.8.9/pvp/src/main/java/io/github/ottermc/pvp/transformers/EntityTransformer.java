package io.github.ottermc.pvp.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.SetVelocityListener;
import net.minecraft.entity.Entity;

@Transformer(className = "net/minecraft/entity/Entity")
public class EntityTransformer {

	@Injector(target = Target.HEAD, name = "setVelocity(DDD)V")
	public void onSetVelocity(Entity entity, double x, double y, double z, Callback callback) {
		SetVelocityListener.SetVelocityEvent event = new SetVelocityListener.SetVelocityEvent(entity, x, y, z);
		EventBus.fire(event);
	}
}
