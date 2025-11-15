package net.ottermc.window.versions.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.ottermc.window.Logger;
import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;
import net.ottermc.window.util.JsonTool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FabricVersion extends VanillaVersion {

    public static final String TYPE = "fabric";

    public FabricVersion(String name, long lastPlayed) {
        super(name, lastPlayed);
    }

    private String mainClass;
    private File[] libraries;
    private JsonObject librariesObjectCache;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void load(JsonElement element) {
        super.load(element);
        JsonPrimitive mainClassPrimitive = JsonTool.getChildPrimitiveSafe(element, "mainClass");
        if (mainClassPrimitive == null)
            return;
        JsonObject librariesObject = JsonTool.getChildObjectSafe(element, "libraries");
        if (librariesObject == null)
            return;
        librariesObjectCache = librariesObject.deepCopy();
        List<File> libraries = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : librariesObject.entrySet()) {
            String name = entry.getKey();
            JsonElement e = entry.getValue();
            if (!e.isJsonPrimitive())
                continue;
            JsonPrimitive urlPrimitive = e.getAsJsonPrimitive();
            String url = urlPrimitive.getAsString();
            File path = FileTool.resolveLibraryPath(Main.gameDir, name);
            if (path == null)
                continue;
            if (!path.exists()) {
                try {
                    FileTool.download(url, path);
                } catch (IOException ex) {
                    Logger.error(ex);
                }
            }
            libraries.add(path);
        }
        mainClass = mainClassPrimitive.getAsString();
        this.libraries = libraries.toArray(new File[0]);
    }

    @Override
    public void store(JsonObject object) {
        super.store(object);
        object.add("mainClass", new JsonPrimitive(mainClass));
        object.add("libraries", librariesObjectCache);
    }

    @Override
    protected List<String> generateLaunchArguments(List<String> libraries) {
        // Temporary fix, this will probably be changed a wee bit in the future
        String[] array = libraries.toArray(new String[0]);
        for (int i = 0; i < array.length; i++) {
            if (array[i].contains("asm"))
                array[i] = null;
        }
        libraries = Arrays.asList(array);

        List<String> arguments = new ArrayList<>();
        arguments.add(FileTool.getFilePath(Main.gameDir, "jre", javaVersion, "bin", "java").getAbsolutePath());
        arguments.addAll(Arrays.asList(Main.flags));
        arguments.addAll(Arrays.asList(properties));
        arguments.add("-cp");
        String classPath = String.join(File.pathSeparator, libraries);
        classPath += File.pathSeparator + String.join(File.pathSeparator, FileTool.transform(this.libraries, File::getAbsolutePath));
        classPath += File.pathSeparator + FileTool.getFilePath(Main.gameDir, "ottermc", "client.jar").getAbsolutePath();
        arguments.add(classPath);
        arguments.add(mainClass);
        arguments.addAll(Arrays.asList(Main.arguments));
        return arguments;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
