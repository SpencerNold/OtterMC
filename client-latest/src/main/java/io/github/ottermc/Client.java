package io.github.ottermc;

import agent.ClassTransformer;
import agent.ReflectionRequired;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.transformers.InGameHudTransformer;
import io.github.ottermc.transformers.MinecraftClientTransformer;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class Client implements Initializer {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-0.0.1 (latest)";
    @ReflectionRequired
    public static final String TARGET = "latest";

    private static Client instance;

    private final ModuleManager modManager = new ModuleManager();

    @ReflectionRequired
    public Client(File file, ClassTransformer transformer) {
        instance = this;
        transformer.register(InGameHudTransformer.class);
        transformer.register(MinecraftClientTransformer.class);
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

    @Override
    public ModuleManager getModuleManager() {
        return modManager;
    }

    public static ModuleManager getModManager() {
        return instance.getModuleManager();
    }

    @ReflectionRequired
    public static Client getInstance() {
        return instance;
    }
}
