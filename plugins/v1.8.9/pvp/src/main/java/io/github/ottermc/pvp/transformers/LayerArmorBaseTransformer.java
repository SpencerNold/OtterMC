package io.github.ottermc.pvp.transformers;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import io.github.ottermc.pvp.transformers.wrapper.LayerArmorBaseWrapper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;

@Transformer(className = "net/minecraft/client/renderer/entity/layers/LayerArmorBase")
public class LayerArmorBaseTransformer {

	@Injector(target = Target.HEAD, name = "func_177183_a(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/model/ModelBase;FFFFFFF)V")
	public void onFunc_177183_a(LayerArmorBase<ModelBase> layerArmorBase, EntityLivingBase entity, ModelBase modelBase, float f1, float f2, float f3, float f4, float f5, float f6, float f7, Callback callback) {
		LayerArmorBaseWrapper.func_177183_a(layerArmorBase, entity, modelBase, f1, f2, f3, f4, f5, f6, f7);
		callback.setCanceled(true);
	}
}
