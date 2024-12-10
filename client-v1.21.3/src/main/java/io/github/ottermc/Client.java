package io.github.ottermc;

import agent.ReflectionRequired;
import agent.transformation.ClassAdapter;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.transformers.MinecraftClientTransformer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.io.File;

public class Client {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-0.0.1 (1.21.3)", TARGET = "1.21.3";

    public Client(File file, ClassAdapter adapter) {
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
}
