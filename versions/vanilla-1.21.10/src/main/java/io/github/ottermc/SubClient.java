package io.github.ottermc;

import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.world.BiomeFinder;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.transformer.TransformerRegistry;
import io.github.ottermc.transformers.InGameHudTransformer;
import io.github.ottermc.transformers.MinecraftClientTransformer;
import io.github.ottermc.universal.Math;
import io.github.ottermc.universal.*;
import io.github.ottermc.universal.hud.*;
import io.ottermc.transformer.ReflectionRequired;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class SubClient extends AbstractSubClient {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-1.0.0 (latest)";
    public static final String TARGET = "1.21.10";

    private static SubClient instance;

    private final ModuleManager modManager = new ModuleManager();
    private final KeybindManager keyManager = new KeybindManager();
    private final HudManager hudManager = new ClientHudManager();

    private final File clientDirectory;

    @ReflectionRequired
    public SubClient(File file, TransformerRegistry registry) {
        registerBindings();
        instance = this;
        this.clientDirectory = file;
        registerTransformers(registry);
    }

    @Override
    @ReflectionRequired
    public void start() {
    }

    @Override
    @ReflectionRequired
    public void onPostInit() {
        registerModules();
        registerDisplays();
        MinecraftClient.getInstance().getWindow().setTitle(NAME + " " + VERSION);
    }

    private void registerBindings() {
        UniversalLog4j.register(new Log4j());
        UKeyboard.register(new GLFWKeyboard());
        UDrawable.register(new ClientDrawable());
        UGameSettings.register(new ClientGameSettings());
        UKeyRegistry.register(new ClientKeyRegistry());
        UMinecraft.register(new ClientMinecraft());
        UVersion.register(new ClientVersion());
        Mth.register(new Math());
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

    @Override
    public KeybindManager getKeybindManager() {
        return instance.keyManager;
    }

    @Override
    public HudManager getHudManager() {
        return instance.hudManager;
    }

    @Override
    public String getIdentifier() {
        return String.join("%s (%s) v%s", NAME, TARGET, VERSION);
    }

    @ReflectionRequired
    public static SubClient getInstance() {
        return instance;
    }
}
