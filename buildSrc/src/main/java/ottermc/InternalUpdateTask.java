package ottermc;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

public class InternalUpdateTask extends DefaultTask {

    private String version;

    @Option(option = "clientVersion", description = "Sets the client version to update from->to")
    public void setClientVersion(String version) {
        this.version = version;
    }

    @Input
    public String getClientVersion() {
        return version;
    }

    @TaskAction
    public void execute() {
        Pattern pattern = Pattern.compile("^(.+?)->(.+?)$");
        Matcher matcher = pattern.matcher(version);
        if (!matcher.matches())
            throw new GradleScriptException(version + " is not in the form of old->new", new InputMismatchException());
        String oldVersion = "v" + matcher.group(1);
        String newVersion = "v" + matcher.group(2);

        Project project = getProject();
        File dir = project.getProjectDir();

        File client = new File(dir, "client-" + oldVersion);
        if (!client.exists()) {
            throw new GradleScriptException(oldVersion + " must exist as a version", new InputMismatchException());
        }
        File plugins = new File(dir, "plugins" + File.separator + oldVersion);

        // update build scripts
        walk(dir, file -> {
            if (!file.isDirectory()) {
                if (file.getName().endsWith(".gradle.kts")) {
                    try {
                        ArrayList<String> lines = new ArrayList<>();
                        Scanner scanner = new Scanner(new FileInputStream(file));
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.contains(oldVersion)) {
                                line = line.replace(oldVersion, newVersion);
                            }
                            lines.add(line);
                        }
                        scanner.close();
                        PrintWriter writer = new PrintWriter(new FileOutputStream(file));
                        lines.forEach(writer::println);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        throw new GradleScriptException("failed to read build script file", e);
                    }
                }
            }
        });

        // update directory names
        boolean success = updateNameAlias(client, "client-" + newVersion);
        if (plugins.exists())
            success = success && updateNameAlias(plugins, newVersion);
        if (!success) {
            throw new GradleScriptException("failed to rename a directory", new IOException());
        }
    }

    private boolean updateNameAlias(File directory, String name) {
        return directory.renameTo(new File(directory.getParent(), name));
    }

    private void walk(File file, Consumer<File> consumer) {
        consumer.accept(file);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null)
                return;
            for (File f : files)
                walk(f, consumer);
        }
    }

    private static class Box<T> {
        public T item = null;
    }
}
