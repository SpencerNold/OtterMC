package net.ottermc.window.versions;

import com.google.gson.JsonElement;
import net.ottermc.window.Logger;
import net.ottermc.window.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Version {

    private final String name;
    private final long lastPlayed;

    public Version(String name, long lastPlayed) {
        this.name = name;
        this.lastPlayed = lastPlayed;
    }

    public String getName() {
        return name;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public abstract void start();
    public abstract void load(JsonElement element);

    @Override
    public String toString() {
        return name;
    }
}
