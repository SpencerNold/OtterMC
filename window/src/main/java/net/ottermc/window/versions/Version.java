package net.ottermc.window.versions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Version {

    private final String name;
    private long lastPlayed;

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

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public abstract void start();
    public abstract void load(JsonElement element);
    public abstract void store(JsonObject object);
    public abstract String getType();

    @Override
    public String toString() {
        return name;
    }
}
