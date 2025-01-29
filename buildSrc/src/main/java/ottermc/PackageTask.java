package ottermc;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackageTask extends DefaultTask {

    @TaskAction
    public void execute() {
        try {
            Project project = getProject();
            String[] command = generateCommand();
            Process process = new ProcessBuilder().directory(new File(project.getRootDir(), "launcher")).command(command).start();
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                project.getLogger().lifecycle(scanner.nextLine());
            }
            scanner = new Scanner(process.getErrorStream());
            while (scanner.hasNextLine()) {
                project.getLogger().lifecycle(scanner.nextLine());
            }
            File directory = project.getRootDir();

            File wrapperFile = new File(directory, String.join(File.separator, "wrapper", "build", "libs", "wrapper.jar"));
            String wrapperName = "wrapper.jar";
            Pair<String, File> wrapper = new Pair<>(wrapperName, wrapperFile);
            for (String version : VersionRegistry.getVersionNames()) {
                List<Pair<String, File>> targets = new ArrayList<>();
                targets.add(wrapper);

                File clientFile = new File(directory, String.join(File.separator, "client-v" + version, "build", "libs", "client-v" + version + "-remapped-joined.jar"));
                if (!clientFile.exists())
                    continue;
                String clientName = "client-v" + version + ".jar";
                Pair<String, File> client = new Pair<>(clientName, clientFile);
                targets.add(client);

                File pluginDir = new File(directory, String.join(File.separator, "plugins", "v" + version));
                for (File pl : pluginDir.listFiles()) {
                    File pluginFile = new File(pl, String.join(File.separator, "build", "libs", pl.getName() + "-remapped.jar"));
                    String pluginName = pl.getName() + ".jar";
                    Pair<String, File> plugin = new Pair<>(pluginName, pluginFile);
                    targets.add(plugin);
                }

                compress(new File(directory, "build-" + version + ".zip"), targets);
            }
        } catch (IOException e) {
            throw new GradleScriptException("failed to build flutter project", e);
        }
    }

    private void compress(File output, List<Pair<String, File>> targets) {
        try {
            ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(output));
            for (Pair<String, File> target : targets) {
                ZipEntry entry = new ZipEntry(target.getKey());
                stream.putNextEntry(entry);
                FileInputStream input = new FileInputStream(target.getValue());
                copy(input, stream);
                input.close();
            }
            stream.close();
        } catch (IOException e) {
            throw new GradleScriptException("failed to compress", e);
        }
    }

    private void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        int n;
        while ((n = input.read(buffer)) != -1)
            output.write(buffer, 0, n);
    }

    private String[] generateCommand() {
        String os = getOperatingSystem();
        List<String> command = new ArrayList<>();
        command.add("flutter");
        command.add("build");
        command.add(os);
        command.add("--release");
        return command.toArray(new String[0]);
    }

    private String getOperatingSystem() {
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("WIN")) {
            return "windows";
        } else if (os.contains("MAC")) {
            return "macos";
        } else {
            return "linux";
        }
    }
}