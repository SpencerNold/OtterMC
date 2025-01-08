package ottermc;

import org.gradle.api.GradleScriptException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Packager {

    public static void runPackageCode(Logger logger, File rootDir) {
        try {
            String[] command = generateCommand();
            Process process = new ProcessBuilder().directory(new File(rootDir, "launcher")).command(command).start();
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                logger.lifecycle(scanner.nextLine());
            }
            scanner = new Scanner(process.getErrorStream());
            while (scanner.hasNextLine()) {
                logger.lifecycle(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new GradleScriptException("failed to build flutter project", e);
        }
    }

    private static String[] generateCommand() {
        String os = getOperatingSystem();
        List<String> command = new ArrayList<>();
        command.add("flutter");
        command.add("build");
        command.add(os);
        command.add("--release");
        return command.toArray(new String[0]);
    }

    private static String getOperatingSystem() {
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