package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.DrawOverlayListener;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;

@Transformer(name = "net/minecraft/client/gui/hud/InGameHud")
public class InGameHudTransformer {

    @Injector(target = Target.TAIL, name = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void onRender(InGameHud inGameHud, DrawContext context, RenderTickCounter tickCounter, Callback callback) {
        EventBus.fire(new DrawOverlayListener.DrawOverlayEvent(context));
    }
}
