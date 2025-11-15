package net.ottermc.window.versions.impl;

import com.google.gson.*;
import net.ottermc.window.Logger;
import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;
import net.ottermc.window.util.JsonTool;
import net.ottermc.window.versions.GameLoader;
import net.ottermc.window.versions.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VanillaVersion extends Version {

    public static final String TYPE = "vanilla";

    private static final Gson GSON = new Gson();

    protected String inheritedVersion;
    protected String javaVersion;
    protected String[] properties;
    protected String clientJar;

    private JsonObject argsObjectCache;

    public VanillaVersion(String name, long lastPlayed) {
        super(name, lastPlayed);
    }

    @Override
    public void start() {
        JsonElement element = readInheritedJson();
        if (element == null)
            return;
        List<String> libraries = ensureProperInstallation(element);
        List<String> arguments = generateLaunchArguments(libraries);
        ProcessBuilder builder = new ProcessBuilder(arguments).inheritIO();
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            System.exit(exitCode);
        } catch (IOException | InterruptedException e) {
            Logger.error(e);
        }
    }

    @Override
    public void load(JsonElement element) {
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
        List<String> properties = new ArrayList<>();
        if (argsObject != null) {
            argsObjectCache = argsObject.deepCopy();
            String os = FileTool.getOperatingSystemSimpleString();
            JsonArray osArray = JsonTool.getChildArrayNullOnMissing(argsObject, os);
            if (osArray != null) {
                for (JsonElement e : osArray)
                    properties.add(e.getAsJsonPrimitive().getAsString());
            }
            JsonArray allArray = JsonTool.getChildArrayNullOnMissing(argsObject, "all");
            if (allArray != null) {
                for (JsonElement e : allArray)
                    properties.add(e.getAsJsonPrimitive().getAsString());
            }
        }
        this.properties = properties.toArray(new String[0]);
        inheritedVersion = inheritsPrimitive.getAsString();
        javaVersion = String.valueOf(javaPrimitive.getAsInt());
        clientJar = clientPrimitive.getAsString();
    }

    @Override
    public void store(JsonObject object) {
        object.add("inherits", new JsonPrimitive(inheritedVersion));
        object.add("java", new JsonPrimitive(javaVersion));
        object.add("client", new JsonPrimitive(clientJar));
        object.add("args", argsObjectCache);
    }

    protected List<String> generateLaunchArguments(List<String> libraries) {
        List<String> arguments = new ArrayList<>();
        arguments.add(FileTool.getFilePath(Main.gameDir, "jre", javaVersion, "bin", "java").getAbsolutePath());
        arguments.addAll(Arrays.asList(Main.flags));
        arguments.addAll(Arrays.asList(properties));
        arguments.add("-javaagent:" + FileTool.getFilePath(Main.gameDir, "ottermc", "versions", clientJar).getAbsolutePath());
        arguments.add("-cp");
        arguments.add(String.join(File.pathSeparator, libraries) + File.pathSeparator + FileTool.getFilePath(Main.gameDir, "ottermc", "client.jar"));
        arguments.add("net.minecraft.client.main.Main");
        arguments.addAll(Arrays.asList(Main.arguments));
        return arguments;
    }

    protected List<String> ensureProperInstallation(JsonElement element) {
        List<String> list = new ArrayList<>();
        File nativeDir = GameLoader.ensureSafeNativesDirectory(inheritedVersion);
        GameLoader.ensureProperAssetsIndex(element);
        GameLoader.fixLogConfigFile(element);
        GameLoader.ensureClientDownloaded(list, inheritedVersion, element);
        GameLoader.ensureLibrariesDownloaded(list, nativeDir, element);
        return list;
    }

    protected JsonElement readInheritedJson() {
        File file = FileTool.getFilePath(Main.gameDir, "versions", inheritedVersion, inheritedVersion + ".json");
        if (!file.exists()) {
            Logger.error("failed to find " + inheritedVersion + " json file, please run the game first", 3);
            return null;
        }
        try {
            FileReader reader = new FileReader(file);
            JsonElement element = GSON.fromJson(reader, JsonElement.class);
            reader.close();
            return element;
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
