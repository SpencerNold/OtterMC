package net.ottermc.window.versions;

import net.ottermc.window.Logger;
import net.ottermc.window.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VanillaVersion extends Version {

    public VanillaVersion(String name, String javaVersion, String[] properties, String agentJar) {
        super(name, javaVersion, properties, agentJar);
    }

    @Override
    public void start() {
        List<String> arguments = new ArrayList<>();
        arguments.add(getGameFile(Main.gameDir, "jre", javaVersion, "bin", "java"));
        arguments.addAll(Arrays.asList(Main.properties));
        arguments.addAll(Arrays.asList(Main.flags));
        arguments.addAll(Arrays.asList(properties));
        arguments.add("-javaagent:" + getGameFile(Main.gameDir, "ottermc", "versions", clientJar));
        arguments.add("-cp");
        arguments.add(String.join(File.pathSeparator, Main.classpath) + File.pathSeparator + getGameFile(Main.gameDir, "ottermc", "client.jar"));
        arguments.add("net.minecraft.client.main.Main");
        arguments.addAll(Arrays.asList(Main.arguments));


        ProcessBuilder builder = new ProcessBuilder(arguments).inheritIO();
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            System.exit(exitCode);
        } catch (IOException | InterruptedException e) {
            Logger.error(e);
        }
    }
}
