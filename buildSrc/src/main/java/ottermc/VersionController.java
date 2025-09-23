package ottermc;

import org.gradle.api.GradleScriptException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class VersionController {

    public static File handle(File src, int maxJavaVersion) {
        try {
            if (!src.exists())
                return src;
            File dst = new File(src.getParentFile(), src.getName().replace(".jar", "-safe.jar"));
            if (dst.exists())
                dst.delete();
            JarOutputStream output = new JarOutputStream(new FileOutputStream(dst));
            writeJar(output, src, maxJavaVersion);
            output.close();
            return dst;
        } catch (IOException e) {
            throw new GradleScriptException("failed to version control jar", e);
        }
    }

    private static void writeJar(JarOutputStream output, File file, int maxJavaVersion) throws IOException {
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            entry = new JarEntry(entry.getName());
            if (entry.isDirectory()) {
                output.putNextEntry(entry);
                output.closeEntry();
                continue;
            }
            InputStream input = jar.getInputStream(entry);
            byte[] bytes = input.readAllBytes();
            input.close();
            if (entry.getName().endsWith(".class") || entry.getName().contains("module-info")) {
                int majorVersion = ((bytes[6] & 0xFF) << 8) | (bytes[7] & 0xFF);
                if (majorVersion > maxJavaVersion)
                    continue;
            }
            output.putNextEntry(entry);
            output.write(bytes, 0, bytes.length);
            output.closeEntry();
        }
        jar.close();
    }
}
