package io.github.ottermc;

import io.github.ottermc.c2.ServerController;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.CategoryRegistry;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.modules.impl.utility.Fullbright;
import io.github.ottermc.modules.impl.utility.Zoom;

import java.io.IOException;

public class Game {

    public static Game game;

    private final AbstractSubClient client;

    public Game(AbstractSubClient client) {
        this.client = client;
    }

    public void start() {
        client.start();
        CategoryRegistry.register(CategoryList.values());
        StateRegistry.setState(State.START);
        ServerController.start();
    }

    public void onPostInit() {
        client.onPostInit();
        registerModules();
    }

    public void load() throws IOException {
        client.load();
    }

    public void save() throws IOException {
        client.save();
    }

    private void registerModules() {
        ModuleManager manager = client.getModuleManager();

        manager.register(new Fullbright());
        manager.register(new Zoom());
    }
}
