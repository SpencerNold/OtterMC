package io.github.ottermc;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.GLFWKeyboard;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.keybind.UniversalKeyboard;
import io.github.ottermc.logging.UniversalLog4j;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.CategoryRegistry;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.world.BiomeFinder;
import io.github.ottermc.tools.Log4j;
import io.github.ottermc.transformer.TransformerRegistry;
import io.github.ottermc.transformers.*;
import io.ottermc.transformer.ReflectionRequired;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;

public class SubClient extends AbstractSubClient {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-1.0.0 (latest)";
    @ReflectionRequired
    public static final String TARGET = "latest";

    private static SubClient instance;

    private final ModuleManager modManager = new ModuleManager();
    private final KeybindManager keyManager = new KeybindManager();

    private final ClientStorage storage;
    private final File clientDirectory;

    @ReflectionRequired
    public SubClient(File file, TransformerRegistry registry) {
        CategoryRegistry.register(CategoryList.values());
        UniversalLog4j.register(new Log4j());
        instance = this;
        this.clientDirectory = file;
        this.storage = new ClientStorage(clientDirectory, String.join(" ", NAME, VERSION, TARGET));
        registry.register(ClientConnectionTransformer.class);
        registry.register(DecoderHandlerTransformer.class);
        registry.register(EncoderHandlerTransformer.class);
        registry.register(InGameHudTransformer.class);
        registry.register(MinecraftClientTransformer.class);
        registry.register(GameRendererTransformer.class);
    }

    @Override
    @ReflectionRequired
    public void start() {
        UniversalKeyboard.register(new GLFWKeyboard());
        registerEvents();
    }

    @Override
    @ReflectionRequired
    public void onPostInit() {
        registerModules();
        MinecraftClient.getInstance().getWindow().setTitle(NAME + " " + VERSION);
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

    private void registerEvents() {
        EventBus.add(new InitializationManager(this));
    }

    private void registerModules() {
        ModuleManager manager = SubClient.getInstance().getModuleManager();
        // Client
        // Game
        // Movement
        // World
        manager.register(new BiomeFinder());
    }

    @Override
    public ModuleManager getModuleManager() {
        return modManager;
    }

    @Override
    public KeybindManager getKeybindManager() {
        return keyManager;
    }

    @Override
    public File getClientDirectory() {
        return clientDirectory;
    }

    public ClientStorage getStorage() {
        return storage;
    }

    @ReflectionRequired
    public static SubClient getInstance() {
        return instance;
    }
}
