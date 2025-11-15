package net.ottermc.window.manifest;

import com.google.gson.*;
import net.ottermc.window.util.JsonTool;
import net.ottermc.window.versions.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

final class ManifestReader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, Class<? extends Version>> registry = new HashMap<>();
    private final File file;

    ManifestReader(File file) {
        this.file = file;
    }

    void addToRegistry(String name, Class<? extends Version> clazz) {
        registry.put(name, clazz);
    }

    Manifest read() {
        Manifest manifest = new Manifest();
        JsonElement element = readElementIgnoreError(file);
        if (element == null)
            return manifest;
        JsonArray versionsArray = JsonTool.getChildArraySafe(element, "versions");
        if (versionsArray == null)
            return manifest;
        for (JsonElement e : versionsArray) {
            JsonPrimitive typePrimitive = JsonTool.getChildPrimitiveSafe(e, "type");
            if (typePrimitive == null)
                return manifest;
            JsonPrimitive namePrimitive = JsonTool.getChildPrimitiveSafe(e, "name");
            if (namePrimitive == null)
                return manifest;
            JsonPrimitive lastPlayedPrimitive = JsonTool.getChildPrimitiveSafe(e, "lastPlayed");
            if (lastPlayedPrimitive == null)
                return manifest;
            String type = typePrimitive.getAsString();
            String name = namePrimitive.getAsString();
            long lastPlayed = lastPlayedPrimitive.getAsLong();
            Version version = newInstanceSafe(type, name, lastPlayed);
            if (version == null)
                return manifest;
            version.load(e);
            manifest.add(version);
        }
        return manifest;
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

    private JsonElement readElementIgnoreError(File file) {
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
