package io.github.ottermc.pvp.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.DrawSelectionBoxListener;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

@Transformer(className = "net/minecraft/client/renderer/RenderGlobal")
public class RenderGlobalTransformer {

	@Injector(name = "drawSelectionBox(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/MovingObjectPosition;IF)V", target = Target.HEAD)
	public void onDrawSelectionBox(RenderGlobal renderGlobal, EntityPlayer player, MovingObjectPosition target, int i, float partialTicks, Callback callback) {
		DrawSelectionBoxListener.DrawSelectionBoxEvent event = new DrawSelectionBoxListener.DrawSelectionBoxEvent(player, target, i, partialTicks);
		EventBus.fire(event);
		callback.setCanceled(event.isCanceled());
	}
}
