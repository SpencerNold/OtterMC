package io.github.ottermc.smp.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.smp.listeners.RenderWorldListener;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;

@Transformer(className = "net/minecraft/client/render/GameRenderer")
public class GameRendererTransformer {

    @Injector(target = Target.TAIL, name = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void onRenderWorld(GameRenderer renderer, RenderTickCounter counter, Callback callback) {
        MatrixStack matrixStack = new MatrixStack();
        
        RenderWorldListener.RenderWorldEvent event = new RenderWorldListener.RenderWorldEvent(counter);
        EventBus.fire(event);
    }
}
