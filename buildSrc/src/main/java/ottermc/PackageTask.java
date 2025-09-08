package ottermc;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PackageTask extends DefaultTask {

    @TaskAction
    public void execute() {
        Project project = getProject();
        File directory = project.getRootDir();
        String os = getOperatingSystem();
        File distributor = new File(directory, "distributors" + File.separator + os + File.separator + "generate." + (os.equals("windows") ? "bat" : "sh"));
        try {
            String[] command = os.equals("windows") ?
                    new String[] { "cmd.exe", "/c", distributor.getAbsolutePath() } :
                    new String[] { "/bin/bash", distributor.getAbsolutePath() };
            Process process = new ProcessBuilder(command)
                    .directory(directory)
                    .start();
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine())
                System.out.println(scanner.nextLine());
            scanner = new Scanner(process.getErrorStream());
            while (scanner.hasNextLine())
                System.out.println(scanner.nextLine());
            //int exitCode = process.waitFor();
            //if (exitCode != 0)
            //    throw new GradleScriptException("failed to execute generate command", new RuntimeException("exit code returned: " + exitCode));
        } catch (IOException e) {
            throw new GradleScriptException("failed to execute generate command", e);
        }
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