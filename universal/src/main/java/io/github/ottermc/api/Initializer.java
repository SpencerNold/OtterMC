package io.github.ottermc.api;

import io.github.ottermc.modules.ModuleManager;

public interface Initializer {
    void start();
    void onPostInit();
    ModuleManager getModuleManager();
}
