package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.SetVelocityListener.SetVelocityEvent;
import net.minecraft.entity.Entity;

@Transformer(name = "net/minecraft/entity/Entity")
public class EntityTransformer {

	@Injector(target = Target.HEAD, name = "setVelocity(DDD)V")
	public void onSetVelocity(Entity entity, double x, double y, double z, Callback callback) {
		SetVelocityEvent event = new SetVelocityEvent(entity, x, y, z);
		EventBus.fire(event);
	}
}
