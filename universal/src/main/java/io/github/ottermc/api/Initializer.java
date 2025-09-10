package io.github.ottermc.api;

import io.github.ottermc.modules.ModuleManager;

import java.io.File;
import java.io.IOException;

public interface Initializer {

    void start();

    void onPostInit();

    void load() throws IOException;

    void save() throws IOException;

    File getClientDirectory();

    ModuleManager getModuleManager();

}
