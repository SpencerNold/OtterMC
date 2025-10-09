package net.ottermc.window.versions;

import net.ottermc.window.Logger;
import net.ottermc.window.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Version {

    private final String name;
    protected final String javaVersion;
    protected final String[] properties;
    protected final String clientJar;

    public Version(String name, String javaVersion, String[] properties, String clientJar) {
        this.name = name;
        this.javaVersion = javaVersion;
        this.properties = properties;
        this.clientJar = clientJar;
    }

    public String getName() {
        return name;
    }

    public abstract void start();

    protected String getGameFile(File gameDir, String... path) {
        return new File(gameDir, String.join(File.separator, path)).getAbsolutePath();
    }

    @Override
    public String toString() {
        return name;
    }
}
