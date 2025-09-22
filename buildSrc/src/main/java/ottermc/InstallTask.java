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
        File pluginDir = new File(clientDir, "plugins");
        if (!pluginDir.exists() && !pluginDir.mkdir())
            throw new GradleScriptException("failed to create plugin directory", new IOException());

        File wrapperSrc = new File(projectDir, String.join(File.separator, "wrapper", "build", "libs", "wrapper-all.jar"));
        File wrapperDst = new File(gameDir, String.join(File.separator, "libraries", "io", "github", "ottermc", "wrapper", "1.0.0", "wrapper-1.0.0.jar"));
        try {
            copy(wrapperSrc, wrapperDst);

            String v189 = VersionRegistry.translateVersionToNameString(Constants.VERSION_1_8_9);
            String vnew = VersionRegistry.translateVersionToNameString(Constants.VERSION_LATEST);

            copyVersion(v189, projectDir, clientDir);
            copyVersion(vnew, projectDir, clientDir);

            copyPlugin(projectDir, pluginDir, String.format("pvp-%s.jar", v189), "plugins", v189, "pvp", "build", "libs", "pvp-remapped.jar");
            copyPlugin(projectDir, pluginDir, String.format("pvp-export-%s.jar", v189), "plugins", v189, "pvp-export", "build", "libs", "pvp-export-remapped.jar");
            copyPlugin(projectDir, pluginDir, String.format("smp-%s.jar", vnew), "plugins", vnew, "smp", "build", "libs", "smp-remapped.jar");
            copyPlugin(projectDir, pluginDir, String.format("smp-export-%s.jar", vnew), "plugins", vnew, "smp-export", "build", "libs", "smp-export-remapped.jar");
        } catch (IOException e) {
            throw new GradleScriptException("failed to copy wrapper", e);
        }
    }

    private void copyVersion(String version, File projectDir, File clientDir) throws IOException {
        File src = new File(projectDir, String.join(File.separator, "client-" + version, "build", "libs", String.format("client-%s-remapped-joined.jar", version)));
        File dst = new File(clientDir, String.format("client-%s.jar", version));
        copy(src, dst);
    }

    private void copyPlugin(File projectDir, File pluginsDir, String name, String... path) throws IOException {
        File src = new File(projectDir, String.join(File.separator, path));
        File dst = new File(pluginsDir, name);
        copy(src, dst);
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
