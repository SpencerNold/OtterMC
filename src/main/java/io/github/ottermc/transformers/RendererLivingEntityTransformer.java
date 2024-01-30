package io.github.ottermc.transformers;

import agent.Callback;
import agent.Injector;
import agent.Target;
import agent.Transformer;
import io.github.ottermc.transformers.wrapper.RendererLivingEntityWrapper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

@Transformer(name = "net/minecraft/client/renderer/entity/RendererLivingEntity")
public class RendererLivingEntityTransformer {

	@Injector(name = "setBrightness(Lnet/minecraft/entity/EntityLivingBase;FZ)Z", target = Target.HEAD)
	public boolean onSetBrightness(RendererLivingEntity<?> renderer, EntityLivingBase entity, float partialTicks, boolean combineTexture, Callback callback) {
		callback.setCanceled(true);
		return RendererLivingEntityWrapper.setBrightness(renderer, entity, partialTicks, combineTexture);
	}
}
