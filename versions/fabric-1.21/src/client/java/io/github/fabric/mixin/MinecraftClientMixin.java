package io.github.fabric.mixin;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.ClickMouseListener;
import io.github.ottermc.events.listeners.PostInitializeListener;
import io.github.ottermc.events.listeners.RightClickMouseListener;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.logging.Logger;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void onTick(CallbackInfo info) {
        RunTickListener.RunTickEvent event = new RunTickListener.RunTickEvent(MinecraftClient.getInstance());
        EventBus.fire(event);
    }

    @Inject(at = @At("HEAD"), method = "doAttack()Z", cancellable = true)
    public void onDoAttack(CallbackInfoReturnable<Boolean> info) {
        ClickMouseListener.ClickMouseEvent event = new ClickMouseListener.ClickMouseEvent();
        EventBus.fire(event);
        if (event.isCanceled()) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "doItemUse()V", cancellable = true)
    public void onDoItemUse(CallbackInfo info) {
        RightClickMouseListener.RightClickMouseEvent event = new RightClickMouseListener.RightClickMouseEvent();
        EventBus.fire(event);
        if (event.isCanceled())
            info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "onInitFinished(Lnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/lang/Runnable;")
    public void onInitFinished(@Coerce Object loadingContext, CallbackInfoReturnable<Runnable> runnable) {
        PostInitializeListener.PostInitializeEvent event = new PostInitializeListener.PostInitializeEvent();
        EventBus.fire(event);
    }
}
