package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.RenderWorldListener;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;

@Transformer(className = "net/minecraft/client/render/GameRenderer")
public class GameRendererTransformer {

    @Injector(target = Target.TAIL, name = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void onRenderWorld(GameRenderer renderer, RenderTickCounter counter, Callback callback) {
        RenderWorldListener.RenderWorldEvent event = new RenderWorldListener.RenderWorldEvent(counter);
        EventBus.fire(event);
    }
}
