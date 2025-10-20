package io.github.ottermc;

import io.github.ottermc.keybind.KeybindManager;
import io.github.ottermc.modules.ModuleManager;
import io.github.ottermc.render.hud.HudManager;

import java.io.File;
import java.io.IOException;

public abstract class AbstractSubClient {

    public abstract void start();
    public abstract void onPostInit();

    public abstract ModuleManager getModuleManager();
    public abstract KeybindManager getKeybindManager();
    public abstract HudManager getHudManager();
    public abstract File getClientDirectory();
    public abstract String getIdentifier();
}
