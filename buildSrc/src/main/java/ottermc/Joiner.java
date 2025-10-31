package ottermc;

import org.gradle.api.GradleScriptException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Joiner {

    private static final Map<String, Boolean> existingEntries = new HashMap<>();

    public static File joinJars(File src, List<String> paths) {
        return joinJars(src, paths, Strategy.PRECEDENCE_NO_REPLACE);
    }

    public static File joinJars(File src, List<String> paths, Strategy strategy) {
        try {
            if (!src.exists())
                return src;
            File dst = new File(src.getParentFile(), src.getName().replace(".jar", "-joined.jar"));
            if (dst.exists())
                dst.delete();
            JarOutputStream output = new JarOutputStream(new FileOutputStream(dst));
            strategy.join(output, src, paths);
            output.close();
            existingEntries.clear();
            return dst;
        } catch (IOException e) {
            throw new GradleScriptException("failed to join jars: " + e.getMessage(), e);
        }
    }

    private static void writeJar(JarOutputStream output, File file) throws IOException {
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (Joiner.existingEntries.containsKey(entry.getName()))
                continue;
            Joiner.existingEntries.put(entry.getName(), true);
            JarEntry e = new JarEntry(entry.getName());
            if (entry.isDirectory()) {
                output.putNextEntry(e);
                output.closeEntry();
                continue;
            }
            InputStream input = jar.getInputStream(entry);
            byte[] bytes = input.readAllBytes();
            input.close();
            output.putNextEntry(e);
            output.write(bytes, 0, bytes.length);
            output.closeEntry();
        }
        jar.close();
    }

    @FunctionalInterface
    public interface Strategy {
        Strategy PRECEDENCE_NO_REPLACE = (output, src, paths) -> {
            writeJar(output, src);
            for (String s : paths) {
                File f = new File(s);
                writeJar(output, f);
            }
        };

        void join(JarOutputStream output, File src, List<String> paths) throws IOException;
    }
}
