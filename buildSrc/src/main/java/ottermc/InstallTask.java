package ottermc;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import java.io.*;

public class InstallTask extends DefaultTask {

    @TaskAction
    public void execute() {
        File projectDir = getProject().getProjectDir();
        File gameDir = BuildTool.getMinecraftDirectory();

        File clientDir = new File(gameDir, "ottermc");
        if (!clientDir.exists() && !clientDir.mkdir())
            throw new GradleScriptException("failed to create client directory", new IOException());
        File versionsDir = new File(clientDir, "versions");
        if (!versionsDir.exists() && !versionsDir.mkdir())
            throw new GradleScriptException("failed to create plugin directory", new IOException());

        File wrapperSrc = getDirFile(projectDir, "wrapper", "build", "libs", "wrapper.jar");
        File wrapperDst = getDirFile(gameDir, "libraries", "io", "github", "ottermc", "wrapper", "1.0.0", "wrapper-1.0.0.jar");

        File windowSrc = getDirFile(projectDir, "window", "build", "libs", "window.jar");
        File windowDst = getDirFile(gameDir, "ottermc", "window.jar");

        File clientSrc = getDirFile(projectDir, "client", "build", "libs", "client-joined.jar");
        File clientDst = getDirFile(gameDir, "ottermc", "client.jar");

        File client189Src = getDirFile(projectDir, "versions", "vanilla-1.8.9", "build", "libs", "vanilla-1.8.9-remapped-joined-safe.jar");
        File client189Dst = getDirFile(gameDir, "ottermc", "versions", "vanilla-1.8.9.jar");

        File client12110Src = getDirFile(projectDir, "versions", "vanilla-1.21.10", "build", "libs", "vanilla-1.21.10-remapped-joined-safe.jar");
        File client12110Dst = getDirFile(gameDir, "ottermc", "versions", "vanilla-1.21.10.jar");

        File fabric12110Src = getDirFile( projectDir,"versions", "fabric-1.21.10", "build", "libs", "omc-1.0.0.jar");
        File fabric12110Dst = getDirFile(gameDir, "mods", "fabric-1.21.10.jar");
        File fabricClientDst = getDirFile(projectDir, "versions", "fabric-1.21.10", "libs", "client.jar");
        try {
            copy(wrapperSrc, wrapperDst);
            copy(windowSrc, windowDst);
            copy(clientSrc, clientDst);
            copy(client189Src, client189Dst);
            copy(client12110Src, client12110Dst);
            copy(fabric12110Src, fabric12110Dst);
            copy(clientSrc, fabricClientDst);
        } catch (IOException e) {
            throw new GradleScriptException("failed to copy wrapper", e);
        }
    }

    private File getDirFile(File dir, String... paths) {
        return new File(dir, String.join(File.separator, paths));
    }

    private void copy(File src, File dst) throws IOException {
        dst.getParentFile().mkdirs();
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
