package net.ottermc.window.versions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.ottermc.window.Logger;
import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;
import net.ottermc.window.util.JsonTool;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameLoader {

    public static void fixLogConfigFile(JsonElement element) {
        JsonObject loggingObject = JsonTool.getChildObjectSafe(element, "logging");
        if (loggingObject == null)
            return;
        JsonObject clientObject = JsonTool.getChildObjectSafe(loggingObject, "client");
        if (clientObject == null)
            return;
        JsonObject fileObject = JsonTool.getChildObjectSafe(clientObject, "file");
        if (fileObject == null)
            return;
        JsonPrimitive idPrimitive = JsonTool.getChildPrimitiveSafe(fileObject, "id");
        if (idPrimitive == null)
            return;
        JsonPrimitive urlPrimitive = JsonTool.getChildPrimitiveSafe(fileObject, "url");
        if (urlPrimitive == null)
            return;
        String name = idPrimitive.getAsString();
        String url = urlPrimitive.getAsString();
        File file = FileTool.getFilePath(Main.gameDir, "assets", "log_configs", name);
        try {
            if (!file.exists())
                FileTool.download(url, file);
            setLogConfigFile(file.getAbsolutePath());
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private static void setLogConfigFile(String path) {
        for (int i = 0; i < Main.flags.length; i++) {
            if (Main.flags[i].matches("-Dlog4j.configurationFile=.+?$"))
                Main.flags[i] = "-Dlog4j.configurationFile=" + path;
        }
    }

    public static void ensureClientDownloaded(List<String> list, String inheritedVersion, JsonElement element) {
        File file = FileTool.getFilePath(Main.gameDir, "versions", inheritedVersion, inheritedVersion + ".jar");
        list.add(file.getAbsolutePath());
        if (file.exists())
            return;
        JsonObject downloadsElement = JsonTool.getChildObjectSafe(element, "downloads");
        if (downloadsElement == null)
            return;
        JsonObject clientElement = JsonTool.getChildObjectSafe(downloadsElement, "client");
        if (clientElement == null)
            return;
        JsonPrimitive urlPrimitive = JsonTool.getChildPrimitiveSafe(clientElement, "url");
        if (urlPrimitive == null)
            return;
        String url = urlPrimitive.getAsString();
        try {
            Logger.log("installing missing client jar...");
            FileTool.download(url, file);
        } catch (IOException e) {
            Logger.error(e); // NOW we can freak out!
        }
    }

    public static void ensureLibrariesDownloaded(List<String> list, File nativeDir, JsonElement element) {
        JsonArray librariesArray = JsonTool.getChildArraySafe(element, "libraries");
        if (librariesArray == null)
            return;
        for (JsonElement e : librariesArray) {
            JsonPrimitive namePrimitive = JsonTool.getChildPrimitiveSafe(e, "name");
            if (namePrimitive == null)
                continue;
            String name = namePrimitive.getAsString();
            JsonElement downloadsElement = JsonTool.getChildSafe(e, "downloads", true);
            if (downloadsElement == null) {
                Logger.log("skipping library as it is missing elements: " + name);
                continue;
            }
            JsonElement artifactElement = JsonTool.getChildSafe(downloadsElement, "artifact", true);
            if (artifactElement != null) {
                JsonPrimitive pathPrimitive = JsonTool.getChildPrimitiveSafe(artifactElement, "path");
                if (pathPrimitive == null)
                    continue;
                JsonPrimitive urlPrimitive = JsonTool.getChildPrimitiveSafe(artifactElement, "url");
                if (urlPrimitive == null)
                    continue;
                String path = pathPrimitive.getAsString();
                String url = urlPrimitive.getAsString();
                list.add(FileTool.getFilePath(Main.gameDir, "libraries", path).getAbsolutePath());
                ensureLibraryDownload(name, path, url);
            }
            JsonElement classifiersElement = JsonTool.getChildSafe(downloadsElement, "classifiers", true);
            if (classifiersElement != null) {
                JsonElement nativesElement = JsonTool.getChildSafe(e, "natives", true);
                String os = getOperatingSystemName(nativesElement);
                JsonElement osElement = JsonTool.getChildSafe(classifiersElement, os, false);
                if (osElement == null)
                    continue;
                JsonPrimitive pathPrimitive = JsonTool.getChildPrimitiveSafe(osElement, "path");
                if (pathPrimitive == null)
                    continue;
                JsonPrimitive urlPrimitive = JsonTool.getChildPrimitiveSafe(osElement, "url");
                if (urlPrimitive == null)
                    continue;
                String path = pathPrimitive.getAsString();
                String url = urlPrimitive.getAsString();
                ensureNativeDownload(nativeDir, name, path, url);
            }
        }
    }

    private static File ensureLibraryDownload(String name, String path, String url) {
        File file = FileTool.getFilePath(Main.gameDir, "libraries", path);
        if (!file.exists()) {
            try {
                Logger.log("not able to find: " + name + ", downloading...");
                FileTool.download(url, file);
            } catch (IOException e) {
                Logger.error(e);
            }
        }
        return file;
    }

    private static void ensureNativeDownload(File nativeDir, String name, String path, String url) {
        File file = ensureLibraryDownload(name, path, url);
        try {
            FileTool.extractJarToDestination(nativeDir, file, true);
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private static String getOperatingSystemName(JsonElement nativesElement) {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch");
        if (os.contains("win"))
            os = "windows";
        else if (os.contains("mac"))
            os = "osx";
        else if (os.contains("nux"))
            os = "linux";
        if (arch.contains("64"))
            arch = "64";
        else
            arch = "32";
        JsonPrimitive namePrimitive = JsonTool.getChildPrimitiveSafe(nativesElement, os);
        if (namePrimitive == null)
            return null;
        String name = namePrimitive.getAsString();
        name = name.replace("${arch}", arch);
        return name;
    }

    public static void ensureProperAssetsIndex(JsonElement element) {
        JsonPrimitive assetsPrimitive = JsonTool.getChildPrimitiveSafe(element, "assets");
        if (assetsPrimitive == null)
            return;
        String assets = assetsPrimitive.getAsString();
        for (int i = 0; i < Main.arguments.length; i++) {
            if (Main.arguments[i].equals("--assetIndex")) {
                if (i != Main.arguments.length - 1) {
                    Main.arguments[i + 1] = assets;
                }
            }
        }
    }

    public static File ensureSafeNativesDirectory(String inheritedVersion) {
        File file = getNativeDirectory();
        if (file == null) {
            file = FileTool.getFilePath(Main.gameDir, "natives", inheritedVersion);
            updateFlagToNativeDirectory(file);
        }
        FileTool.nuke(file);
        return file;
    }

    private static File getNativeDirectory() {
        Pattern pattern = Pattern.compile("-Djava\\.library\\.path=(.+?)$");
        for (String s : Main.flags) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                return new File(matcher.group(1));
            }
        }
        return null;
    }

    private static void updateFlagToNativeDirectory(File nativeDir) {
        for (int i = 0; i < Main.flags.length; i++) {
            String s = Main.flags[i];
            if (s.matches("-Djna\\.tmpdir=.+?$")
                    || s.matches("-Djava\\.library\\.path=.+?$")
                    || s.matches("-Dorg\\.lwjgl\\.system\\.SharedLibraryExtractPath=.+?$")
                    || s.matches("-Dio\\.netty\\.native\\.workdir=.+?$")
            ) {
                Main.flags[i] = s.replaceAll("=.+?$", "=" + nativeDir.getAbsolutePath().replace('\\', '/'));
            }
        }
    }
}
