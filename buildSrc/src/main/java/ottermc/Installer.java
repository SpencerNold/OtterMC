package ottermc;

import org.gradle.api.GradleScriptException;

import java.io.*;

public class Installer {

    public static void install(File projectDir) {
        File gameDir = Launcher.getMinecraftDirectory();

        File clientDir = new File(gameDir, "ottermc");
        if (!clientDir.exists() && !clientDir.mkdir())
            throw new GradleScriptException("failed to create client directory", new IOException());

        File wrapperSrc = new File(projectDir, String.join(File.separator, "wrapper", "build", "libs", "wrapper.jar"));
        File wrapperDst = new File(gameDir, String.join(File.separator, "libraries", "io", "github", "ottermc", "wrapper", "1.0.0", "wrapper-1.0.0.jar"));
        try {
            copy(wrapperSrc, wrapperDst);

            copyVersion("1.8.9", projectDir, clientDir);
            copyVersion("latest", projectDir, clientDir);
        } catch (IOException e) {
            throw new GradleScriptException("failed to copy wrapper", e);
        }
    }

    private static void copyVersion(String version, File projectDir, File clientDir) throws IOException {
        File src = new File(projectDir, String.join(File.separator, "client-v" + version, "build", "libs", String.format("client-v%s-remapped-joined.jar", version)));
        File dst = new File(clientDir, String.format("client-v%s.jar", version));
        copy(src, dst);
    }

    private static void copy(File src, File dst) throws IOException {
        InputStream input = new FileInputStream(src);
        OutputStream output = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1)
            output.write(buffer, 0, length);
        output.close();
        input.close();
    }
}