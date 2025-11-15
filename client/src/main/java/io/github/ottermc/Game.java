package io.github.ottermc;

import io.github.ottermc.c2.ServerController;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.logging.Logger;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.CategoryRegistry;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.impl.display.UIScheme;
import io.github.ottermc.modules.impl.display.Waypoints;
import io.github.ottermc.modules.impl.hud.*;
import io.github.ottermc.modules.impl.utility.Fullbright;
import io.github.ottermc.modules.impl.utility.Zoom;
import io.github.ottermc.modules.impl.world.ToggleSprint;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.render.hud.MovableComponent;
import io.github.ottermc.render.hud.impl.ToggleSprintHud;
import io.github.ottermc.render.screen.impl.EditHudScreen;
import io.github.ottermc.universal.UDrawable;
import io.github.ottermc.universal.UKeyRegistry;

import java.io.IOException;

public class Game {

    public static final String CLIENT_NAME = "OtterMC";

    public static Game game = null;

    private final AbstractSubClient client;
    private final PersistentStorage clientStorage;

    private boolean started = false, posted = false;

    public Game(AbstractSubClient client) {
        this.client = client;
        this.clientStorage = new PersistentStorage(client.getClientDirectory().getParentFile(), client.getIdentifier());
    }

    public void start() {
        if (started)
            return;
        started = true;
        client.start();
        CategoryRegistry.register(CategoryList.values());
        StateRegistry.setState(State.START);
        ServerController.start();
        registerEvents();
    }

    public void onPostInit() {
        if (posted)
            return;
        posted = true;
        client.onPostInit();
        registerKeybinds();
        registerModules();
        registerDisplays();
        for (Module mod : getModManager().getModules())
            clientStorage.register(mod);
        for (MovableComponent hud : getHudManager().getMovableComponents())
            clientStorage.register(hud);
        load();
        StateRegistry.setState(State.RUNNING);
    }

    public void load() {
        try {
            clientStorage.read();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    public void save() {
        try {
            clientStorage.write();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private void registerKeybinds() {
        KeybindManager manager = getKeyManager();
        manager.register(UKeyRegistry.getKeyRShift(), () -> {
            UDrawable.display(new EditHudScreen());
        });
    }

    private void registerModules() {
        ModuleManager manager = client.getModuleManager();
        // Display
        manager.register(new UIScheme());
        manager.register(new Waypoints());
        // Hud
        manager.register(new ArmorStatus());
        manager.register(new ClickCounter());
        manager.register(new Coordinate());
        manager.register(new KeyStroke());
        manager.register(new PotionEffect());
        // Utility
        manager.register(new Fullbright());
        manager.register(new Zoom());
        // World
        manager.register(new ToggleSprint());
    }

    private void registerEvents() {
        EventBus.add(new InitializationManager());
        EventBus.add(client.getHudManager());
        EventBus.add(client.getKeybindManager());
    }

    private void registerDisplays() {
        HudManager manager = getHudManager();
        manager.register(new ToggleSprintHud());
    }

    public ModuleManager getModManager() {
        return client.getModuleManager();
    }

    public KeybindManager getKeyManager() {
        return client.getKeybindManager();
    }

    public HudManager getHudManager() {
        return client.getHudManager();
    }
}
