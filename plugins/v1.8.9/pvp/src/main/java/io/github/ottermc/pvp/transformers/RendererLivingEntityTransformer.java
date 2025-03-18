package io.github.ottermc.pvp.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.pvp.transformers.wrapper.RendererLivingEntityWrapper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

@Transformer(className = "net/minecraft/client/renderer/entity/RendererLivingEntity")
public class RendererLivingEntityTransformer {

	@Injector(name = "setBrightness(Lnet/minecraft/entity/EntityLivingBase;FZ)Z", target = Target.HEAD)
	public boolean onSetBrightness(RendererLivingEntity<?> renderer, EntityLivingBase entity, float partialTicks, boolean combineTexture, Callback callback) {
		callback.setCanceled(true);
		return RendererLivingEntityWrapper.setBrightness(renderer, entity, partialTicks, combineTexture);
	}
}
