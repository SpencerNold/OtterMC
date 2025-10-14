package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderGameOverlayListener;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;

@Transformer(className = "net/minecraft/client/gui/hud/InGameHud")
public class InGameHudTransformer {

    @Injector(target = Target.TAIL, name = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void onRender(InGameHud inGameHud, DrawContext context, RenderTickCounter tickCounter, Callback callback) {
        EventBus.fire(new RenderGameOverlayListener.RenderGameOverlayEvent(context, 0.0f));
    }
}
