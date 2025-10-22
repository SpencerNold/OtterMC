package net.ottermc.window.versions;

import com.google.gson.*;
import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;
import net.ottermc.window.util.JsonTool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class VersionLoader {

    private static final Gson GSON = new Gson();

    private final Map<String, Class<? extends Version>> registry = new HashMap<>();

    public void register(String type, Class<? extends Version> clazz) {
        registry.put(type, clazz);
    }

    private Version newInstanceSafe(String type, String name, long lastPlayed) {
        try {
            Class<? extends Version> clazz = registry.get(type);
            Constructor<? extends Version> constructor = clazz.getDeclaredConstructor(String.class, long.class);
            return constructor.newInstance(name, lastPlayed);
        } catch (Exception e) {
            return null;
        }
    }

    public void loadManifest() {
        File file = FileTool.getFilePath(Main.gameDir, "ottermc", "manifest.json");
        JsonElement element = readElementIgnoreError(file);
        if (element == null)
            return;
        JsonArray versionsArray = JsonTool.getChildArraySafe(element, "versions");
        if (versionsArray == null)
            return;
        for (JsonElement e : versionsArray) {
            JsonPrimitive typePrimitive = JsonTool.getChildPrimitiveSafe(e, "type");
            if (typePrimitive == null)
                return;
            JsonPrimitive namePrimitive = JsonTool.getChildPrimitiveSafe(e, "name");
            if (namePrimitive == null)
                return;
            JsonPrimitive lastPlayedPrimitive = JsonTool.getChildPrimitiveSafe(e, "lastPlayed");
            if (lastPlayedPrimitive == null)
                return;
            String type = typePrimitive.getAsString();
            String name = namePrimitive.getAsString();
            long lastPlayed = lastPlayedPrimitive.getAsLong();
            Version version = newInstanceSafe(type, name, lastPlayed);
            if (version == null)
                return;
            version.load(e);
            Main.versions.add(version);
        }
        Main.versions.sort(Comparator.comparingLong(v -> ((Version) v).getLastPlayed()).reversed());
    }

    private static JsonElement readElementIgnoreError(File file) {
        try {
            FileReader reader = new FileReader(file);
            JsonElement element = GSON.fromJson(reader, JsonElement.class);
            reader.close();
            return element;
        } catch (IOException e) {
            return null;
        }
    }
}
