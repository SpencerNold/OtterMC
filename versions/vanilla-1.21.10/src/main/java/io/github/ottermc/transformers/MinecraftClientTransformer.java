package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.ClickMouseListener;
import io.github.ottermc.events.listeners.PostInitializeListener;
import io.github.ottermc.events.listeners.RightClickMouseListener;
import io.github.ottermc.events.listeners.RunTickListener;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.client.MinecraftClient;

@Transformer(className = "net/minecraft/client/MinecraftClient")
public class MinecraftClientTransformer {

    @Injector(target = Target.HEAD, name = "tick()V")
    public void onTick(MinecraftClient client, Callback callback) {
        RunTickListener.RunTickEvent event = new RunTickListener.RunTickEvent(client);
        EventBus.fire(event);
    }

    @Injector(target = Target.HEAD, name = "doAttack()Z")
    public boolean onDoAttack(MinecraftClient client, Callback callback) {
        EventBus.fire(new ClickMouseListener.ClickMouseEvent());
        return false;
    }

    @Injector(target = Target.HEAD, name = "doItemUse()V")
    public void onDoItemUse(MinecraftClient client, Callback callback) {
        EventBus.fire(new RightClickMouseListener.RightClickMouseEvent());
    }

    @Injector(target = Target.HEAD, name = "onInitFinished(Lnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/lang/Runnable;")
    public Runnable onInitFinished(MinecraftClient client, Object loadingContext, Callback callback) {
        PostInitializeListener.PostInitializeEvent event = new PostInitializeListener.PostInitializeEvent();
        EventBus.fire(event);
        return null;
    }
}
