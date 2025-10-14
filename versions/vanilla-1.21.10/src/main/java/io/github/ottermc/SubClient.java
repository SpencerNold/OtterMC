package io.github.ottermc;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.world.BiomeFinder;
import io.github.ottermc.render.hud.Component;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.render.hud.MovableComponent;
import io.github.ottermc.transformer.TransformerRegistry;
import io.github.ottermc.transformers.InGameHudTransformer;
import io.github.ottermc.transformers.MinecraftClientTransformer;
import io.github.ottermc.universal.*;
import io.github.ottermc.universal.hud.*;
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
    private final HudManager hudManager = new ClientHudManager();

    private final ClientStorage storage;
    private final File clientDirectory;

    @ReflectionRequired
    public SubClient(File file, TransformerRegistry registry) {
        registerBindings();
        instance = this;
        this.clientDirectory = file;
        this.storage = new ClientStorage(clientDirectory, String.join(" ", NAME, VERSION, TARGET));
        registerTransformers(registry);
    }

    @Override
    @ReflectionRequired
    public void start() {
        registerEvents();
    }

    @Override
    @ReflectionRequired
    public void onPostInit() {
        registerModules();
        registerDisplays();
        MinecraftClient.getInstance().getWindow().setTitle(NAME + " " + VERSION);
    }

    @Override
    public void load() throws IOException {
        storage.clear();
        for (Module module : getModuleManager().getModules())
            storage.writable(module);
        for (Component component : getHudManager().getComponents()) {
            if (component instanceof MovableComponent)
                storage.writable((MovableComponent) component);
        }
        storage.read();
    }

    @Override
    public void save() throws IOException {
        storage.write();
    }

    private void registerBindings() {
        UniversalLog4j.register(new Log4j());
        UKeyboard.register(new GLFWKeyboard());
        UDrawable.register(new ClientDrawable());
        UGameSettings.register(new ClientGameSettings());
        UKeyRegistry.register(new ClientKeyRegistry());
        UMinecraft.register(new ClientMinecraft());
        UVersion.register(new ClientVersion());
    }

    private void registerTransformers(TransformerRegistry registry) {
        registry.register(InGameHudTransformer.class);
        registry.register(MinecraftClientTransformer.class);
    }

    private void registerDisplays() {
        hudManager.register(new ClientArmorStatusHud());
        hudManager.register(new ClientPotionEffectHud());
        hudManager.register(new ClientKeyStrokeHud());
        hudManager.register(new ClientCoordinateHud());
        hudManager.register(new ClientClickCounterHud());
    }

    private void registerEvents() {
        EventBus.add(new InitializationManager());
        EventBus.add(hudManager);
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
    public File getClientDirectory() {
        return clientDirectory;
    }

    public ClientStorage getStorage() {
        return storage;
    }

    @Override
    public KeybindManager getKeybindManager() {
        return instance.keyManager;
    }

    public HudManager getHudManager() {
        return instance.hudManager;
    }

    @ReflectionRequired
    public static SubClient getInstance() {
        return instance;
    }
}
