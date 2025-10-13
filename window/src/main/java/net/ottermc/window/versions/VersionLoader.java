package net.ottermc.window.versions;

import com.google.gson.*;
import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;
import net.ottermc.window.util.JsonTool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

public class VersionLoader {

    private static final Gson GSON = new Gson();

    public static void loadManifest() {
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
            if (type.equals("vanilla")) {
                loadVanillaVersion(name, lastPlayed, e);
            }
        }
        Main.versions.sort(Comparator.comparingLong(v -> ((Version) v).getLastPlayed()).reversed());
    }

    private static void loadVanillaVersion(String name, long lastPlayed, JsonElement element) {
        JsonPrimitive inheritsPrimitive = JsonTool.getChildPrimitiveSafe(element, "inherits");
        if (inheritsPrimitive == null)
            return;
        JsonPrimitive javaPrimitive = JsonTool.getChildPrimitiveSafe(element, "java");
        if (javaPrimitive == null)
            return;
        JsonPrimitive clientPrimitive = JsonTool.getChildPrimitiveSafe(element, "client");
        if (clientPrimitive == null)
            return;
        JsonObject argsObject = JsonTool.getChildObjectNullOnMissing(element, "args");
        String[] args = new String[] {};
        if (argsObject != null) {
            String os = FileTool.getOperatingSystemSimpleString();
            JsonArray osArray = JsonTool.getChildArrayNullOnMissing(argsObject, os);
            if (osArray != null) {
                args = new String[osArray.size()];
                int index = 0;
                for (JsonElement e : osArray) {
                    args[index] = e.getAsJsonPrimitive().getAsString();
                    index++;
                }
            }
        }
        String inherits = inheritsPrimitive.getAsString();
        String java = String.valueOf(javaPrimitive.getAsInt());
        String client = clientPrimitive.getAsString();
        VanillaVersion version = new VanillaVersion(name, lastPlayed, inherits, java, args, client);
        Main.versions.add(version);
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
