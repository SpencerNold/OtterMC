package io.github.ottermc;

import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;

import java.io.File;
import java.io.IOException;

public abstract class AbstractSubClient {

    public static AbstractSubClient instance;

    public abstract void start();
    public abstract void onPostInit();

    public abstract void load() throws IOException;
    public abstract void save() throws IOException;

    public abstract ModuleManager getModuleManager();
    public abstract KeybindManager getKeybindManager();
    public abstract File getClientDirectory();

}
