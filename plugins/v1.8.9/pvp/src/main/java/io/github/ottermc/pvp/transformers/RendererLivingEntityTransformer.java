package io.github.ottermc.pvp.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.pvp.transformers.wrapper.RendererLivingEntityWrapper;
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
