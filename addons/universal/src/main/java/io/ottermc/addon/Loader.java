package io.ottermc.addon;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class Loader {

    private static ExceptionHandler handler = Throwable::printStackTrace;

    public static Addon loadAddonFile(File file) {
        try {
            if (!file.exists())
                return null;
            InputStream input = Files.newInputStream(file.toPath());
            Addon addon = load(input);
            input.close();
            return addon;
        } catch (IOException e) {
            handler.handle(e);
            return null;
        }
    }

    public static Addon loadAddonResource(String name) {
        try {
            InputStream input = Loader.class.getResourceAsStream(name);
            if (input == null)
                return null;
            Addon addon = load(input);
            input.close();
            return addon;
        } catch (IOException e) {
            handler.handle(e);
            return null;
        }
    }

    public static Addon load(InputStream input) throws IOException {
        return new ObjectMapper().readValue(input, Addon.class);
    }

    public static void install(Addon addon, File gameDir) {
        for (Library library : addon.getLibraries()) {
            String name = library.getName();
            String url = library.getURL();
            try {
                File destination = getLibraryPath(library, gameDir);
                if (destination.exists()) {
                    System.out.printf("[Wrapper] %s already exists, skipping!\n", name);
                    return;
                }
                System.out.printf("[Wrapper] installing %s from %s!\n", name, url);
                download(destination, new URL(url));
            } catch (IOException e) {
                handler.handle(e);
            }
        }
    }

    public static String getClassPath(Addon addon, File gameDir) {
        Library[] libraries = addon.getLibraries();
        String[] classPath = new String[libraries.length];
        for (int i = 0; i < libraries.length; i++)
            classPath[i] = getLibraryPath(libraries[i], gameDir).getAbsolutePath();
        return String.join(File.pathSeparator, classPath);
    }

    public static void setExceptionHandler(ExceptionHandler handler) {
        Loader.handler = handler;
    }

    private static void download(File destination, URL url) throws IOException {
        FileOutputStream output = new FileOutputStream(destination);
        InputStream input = url.openStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = input.read(buffer)) != -1)
            output.write(buffer, 0, read);
        input.close();
        output.flush();
        output.close();
    }

    private static File getLibraryPath(Library library, File gameDir) {
        File libsDir = new File(gameDir, "libraries");
        String[] parts = library.getName().split(":");
        if (parts.length < 3)
            throw new IllegalArgumentException("Invalid name: " + library.getName());
        String groupId = parts[0].replace('.', File.separatorChar);
        String artifactId = parts[1];
        String version = parts[2];
        String jarName = artifactId + "-" + version + ".jar";
        return new File(String.join(File.separator, libsDir.getAbsolutePath(), groupId, artifactId, version, jarName));
    }
}
