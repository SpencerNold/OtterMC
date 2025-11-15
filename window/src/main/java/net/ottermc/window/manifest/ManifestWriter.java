package net.ottermc.window.manifest;

import com.google.gson.*;
import net.ottermc.window.versions.Version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ManifestWriter {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File file;

    ManifestWriter(File file) {
        this.file = file;
    }

    void write(Manifest manifest) {
        JsonArray array = new JsonArray();
        for (Version version : manifest.getVersions()) {
            JsonObject object = new JsonObject();
            object.add("name", new JsonPrimitive(version.getName()));
            object.add("type", new JsonPrimitive(version.getType()));
            object.add("lastPlayed", new JsonPrimitive(version.getLastPlayed()));
            version.store(object);
            array.add(object);
        }
        JsonObject object = new JsonObject();
        object.add("versions", array);
        writeElementIgnoreError(file, object);
    }

    private void writeElementIgnoreError(File file, JsonElement element) {
        try {
            FileWriter writer = new FileWriter(file);
            GSON.toJson(element, writer);
            writer.flush();
            writer.close();
        } catch (IOException ignored) {
        }
    }
}
