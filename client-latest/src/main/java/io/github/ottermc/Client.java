package io.github.ottermc;

import agent.ClassTransformer;
import agent.ReflectionRequired;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.GLFWKeyboard;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.transformers.InGameHudTransformer;
import io.github.ottermc.transformers.MinecraftClientTransformer;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;

public class Client implements Initializer {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-1.0.0 (latest)";
    @ReflectionRequired
    public static final String TARGET = "latest";

    private static Client instance;

    private final ModuleManager modManager = new ModuleManager();

    private final ClientStorage storage;
    private final File clientDirectory;

    @ReflectionRequired
    public Client(File file, ClassTransformer transformer) {
        instance = this;
        this.clientDirectory = file;
        this.storage = new ClientStorage(clientDirectory, String.join(" ", NAME, VERSION, TARGET));
        transformer.register(InGameHudTransformer.class);
        transformer.register(MinecraftClientTransformer.class);
    }

    @ReflectionRequired
    public void start() {
        UniversalKeyboard.register(new GLFWKeyboard());
        registerEvents();
    }

    @Override
    public void load() throws IOException {
        storage.clear();
        for (Module module : getModuleManager().getModules())
            storage.writable(module);
        storage.read();
    }

    @Override
    public void save() throws IOException {
        storage.write();
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

    @Override
    public File getClientDirectory() {
        return clientDirectory;
    }

    public ClientStorage getStorage() {
        return storage;
    }

    @ReflectionRequired
    public static Client getInstance() {
        return instance;
    }
}
