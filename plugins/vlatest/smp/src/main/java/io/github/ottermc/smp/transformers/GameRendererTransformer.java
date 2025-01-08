package io.github.ottermc.smp.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.smp.modules.listeners.RenderWorldListener;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;

@Transformer(name = "net/minecraft/client/render/GameRenderer")
public class GameRendererTransformer {

    @Injector(target = Target.TAIL, name = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void onRenderWorld(GameRenderer renderer, RenderTickCounter counter, Callback callback) {
        MatrixStack matrixStack = new MatrixStack();
        
        RenderWorldListener.RenderWorldEvent event = new RenderWorldListener.RenderWorldEvent(counter);
        EventBus.fire(event);
    }
}
