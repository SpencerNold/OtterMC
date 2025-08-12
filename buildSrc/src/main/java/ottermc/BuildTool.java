package ottermc;

import org.gradle.api.GradleScriptException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BuildTool {

    public static File getMinecraftDirectory() {
        if (isWindows())
            return getMinecraftDirectoryWIN();
        else if (isMacOS())
            return getMinecraftDirectoryOSX();
        return getMinecraftDirectoryUNIX();
    }

    private static File getMinecraftDirectoryWIN() {
        return new File(getJoinedWithSeparator(System.getenv("APPDATA"), ".minecraft"));
    }

    private static File getMinecraftDirectoryOSX() {
        return new File(getJoinedWithSeparator(System.getProperty("user.home"), "Library", "Application Support", "minecraft"));
    }

    private static File getMinecraftDirectoryUNIX() {
        // This is very much probably wrong, but I have no idea where .minecraft is stored on linux systems
        return new File(getJoinedWithSeparator(System.getProperty("user.home"), ".minecraft"));
    }

    public static String getJoinedWithSeparator(String... paths) {
        return String.join(File.separator, paths);
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toUpperCase();
        return os.contains("WIN");
    }

    public static boolean isMacOS() {
        String os = System.getProperty("os.name").toUpperCase();
        return os.contains("MAC");
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name").toUpperCase();
        return !isWindows() && !isMacOS(); // Meh
    }

    public static void copy(File src, File dst) {
        try {
            if (!dst.exists())
                dst.createNewFile();
            FileInputStream input = new FileInputStream(src);
            FileOutputStream output = new FileOutputStream(dst);
            int n = -1;
            byte[] buffer = new byte[4096];
            while ((n = input.read(buffer)) != -1)
                output.write(buffer, 0, n);
            input.close();
            output.close();
        } catch (IOException e) {
            throw new GradleScriptException("failed to copy from: " + src.getAbsolutePath() + " to " + dst.getAbsolutePath(), e);
        }
    }

    public static void deleteDirectory(File file) {
        if (file.isFile())
            file.delete();
        else {
            for (File f : file.listFiles()) {
                deleteDirectory(f);
                file.delete();
            }
        }
    }
}
