package io.github.ottermc.pvp.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.DrawSelectionBoxListener;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

@Transformer(name = "net/minecraft/client/renderer/RenderGlobal")
public class RenderGlobalTransformer {

	@Injector(name = "drawSelectionBox(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/MovingObjectPosition;IF)V", target = Target.HEAD)
	public void onDrawSelectionBox(RenderGlobal renderGlobal, EntityPlayer player, MovingObjectPosition target, int i, float partialTicks, Callback callback) {
		DrawSelectionBoxListener.DrawSelectionBoxEvent event = new DrawSelectionBoxListener.DrawSelectionBoxEvent(player, target, i, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
