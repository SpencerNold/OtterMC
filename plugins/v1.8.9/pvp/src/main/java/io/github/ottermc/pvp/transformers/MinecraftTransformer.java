package io.github.ottermc.pvp.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.ClickMouseListener;
import io.github.ottermc.pvp.listeners.RightClickMouseListener;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.Minecraft;

@Transformer(className = "net/minecraft/client/Minecraft")
public class MinecraftTransformer {

    @Injector(target = Target.HEAD, name = "clickMouse()V")
    public void onClickMouse(Minecraft mc, Callback callback) {
        ClickMouseListener.ClickMouseEvent event = new ClickMouseListener.ClickMouseEvent();
        EventBus.fire(event);
    }

    @Injector(target = Target.HEAD, name = "rightClickMouse()V")
    public void onRightClickMouse(Minecraft mc, Callback callback) {
        RightClickMouseListener.RightClickMouseEvent event = new RightClickMouseListener.RightClickMouseEvent();
        EventBus.fire(event);
    }
}
