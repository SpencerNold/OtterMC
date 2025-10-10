package net.ottermc.window.util;

import net.ottermc.window.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileTool {

    private static final int MAX_BUFFER_SIZE = 4096;

    public static File getFilePath(File dir, String... path) {
        File file = new File(dir, String.join(File.separator, path));
        File parent = file.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
            Logger.error("unnable to create parent for: " + file.getAbsolutePath(), 4);
            return file;
        }
        return file;
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        int read;
        while ((read = input.read(buffer)) != -1)
            output.write(buffer, 0, read);
    }

    public static void download(String link, File destination) throws IOException {
        URL url = new URL(link);
        OutputStream output = Files.newOutputStream(destination.toPath());
        InputStream input = url.openStream();
        copy(input, output);
        output.close();
        input.close();
    }

    public static boolean nuke(File file) {
        boolean worked = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    worked = worked && nuke(f);
                }
            }
        }
        return worked && file.delete();
    }

    public static void extractJarToDestination(File destination, File file, boolean skipManifestData) throws IOException {
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (skipManifestData && name.contains("META-INF"))
                continue;
            File out = new File(destination, name);
            if (entry.isDirectory()) {
                if (!out.isDirectory() && !out.mkdirs())
                    throw new IOException("failed to create directory: " + out);
            }
            File parent = out.getParentFile();
            if (parent != null && !parent.isDirectory() && !parent.mkdirs())
                throw new IOException("failed to create parent directory: " + parent);
            InputStream input = jar.getInputStream(entry);
            OutputStream output = Files.newOutputStream(out.toPath());
            copy(input, output);
            input.close();
            output.close();
        }
        jar.close();
    }
}
