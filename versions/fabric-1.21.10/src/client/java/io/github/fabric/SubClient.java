package io.github.fabric;

import io.github.fabric.universal.Math;
import io.github.fabric.universal.*;
import io.github.fabric.universal.hud.*;
import io.github.ottermc.AbstractSubClient;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.universal.*;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class SubClient extends AbstractSubClient {

    public static final String NAME = "OtterMC", VERSION = "ALPHA-1.0.0";
    public static final String TARGET = "Fabric 1.21.10";

    private static SubClient instance;

    private final ModuleManager modManager = new ModuleManager();
    private final KeybindManager keyManager = new KeybindManager();
    private final HudManager hudManager = new ClientHudManager();

    private final File clientDirectory;

    public SubClient() {
        registerBindings();
        instance = this;
        this.clientDirectory = new File(MinecraftClient.getInstance().runDirectory, "ottermc" + File.separator + "versions");
    }

    @Override
    public void start() {
    }

    @Override
    public void onPostInit() {
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

    private void registerDisplays() {
        hudManager.register(new ClientArmorStatusHud());
        hudManager.register(new ClientPotionEffectHud());
        hudManager.register(new ClientKeyStrokeHud());
        hudManager.register(new ClientCoordinateHud());
        hudManager.register(new ClientClickCounterHud());
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

    public static SubClient getInstance() {
        return instance;
    }
}
