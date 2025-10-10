package net.ottermc.window.versions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.ottermc.window.Logger;
import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VanillaVersion extends Version {

    private static final Gson GSON = new Gson();

    private final String inheritedVersion;
    private final String javaVersion;
    private final String[] properties;
    private final String clientJar;

    public VanillaVersion(String name, String inheritedVersion, String javaVersion, String[] properties, String clientJar) {
        super(name);
        this.inheritedVersion = inheritedVersion;
        this.javaVersion = javaVersion;
        this.properties = properties;
        this.clientJar = clientJar;
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

    private List<String> generateLaunchArguments(List<String> libraries) {
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

    private List<String> ensureProperInstallation(JsonElement element) {
        List<String> list = new ArrayList<>();
        File nativeDir = GameLoader.ensureSafeNativesDirectory(inheritedVersion);
        GameLoader.ensureProperAssetsIndex(element);
        GameLoader.fixLogConfigFile(element);
        GameLoader.ensureClientDownloaded(list, inheritedVersion, element);
        GameLoader.ensureLibrariesDownloaded(list, nativeDir, element);
        return list;
    }

    private JsonElement readInheritedJson() {
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
}
