package io.github.ottermc.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.PostInitializeListener;
import io.github.ottermc.events.listeners.RunTickListener;
import net.minecraft.client.MinecraftClient;

@Transformer(name = "net/minecraft/client/MinecraftClient")
public class MinecraftClientTransformer {

    @Injector(target = Target.HEAD, name = "tick()V")
    public void onTick(MinecraftClient client, Callback callback) {
        RunTickListener.RunTickEvent event = new RunTickListener.RunTickEvent(client);
        EventBus.fire(event);
    }

    @Injector(target = Target.HEAD, name = "onInitFinished(Lnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/lang/Runnable;")
    public Runnable onInitFinished(MinecraftClient client, Object loadingContext, Callback callback) {
        PostInitializeListener.PostInitializeEvent event = new PostInitializeListener.PostInitializeEvent();
        EventBus.fire(event);
        return null;
    }
}
