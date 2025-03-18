package io.github.ottermc;

import agent.ReflectionRequired;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.transformers.InGameHudTransformer;
import io.github.ottermc.transformers.MinecraftClientTransformer;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class Client {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-0.0.1 (1.21.4)";
    @ReflectionRequired
    public static final String TARGET = "1.21.4";

    private static Client instance;

    private final ModuleManager modManager = new ModuleManager();

    public Client(File file, ClassAdapter1 adapter) {
        instance = this;
        adapter.register(InGameHudTransformer.class);
        adapter.register(MinecraftClientTransformer.class);
    }

    @ReflectionRequired
    public void start() {
        registerEvents();
    }

    @ReflectionRequired
    public void onPostInit() {
        MinecraftClient.getInstance().getWindow().setTitle(NAME + " " + VERSION);
    }

    private void registerEvents() {
        EventBus.add(new InitializationManager(this));
    }

    public static ModuleManager getModManager() {
        return instance.modManager;
    }
}
