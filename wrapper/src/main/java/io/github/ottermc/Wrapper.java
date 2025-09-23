package io.github.ottermc;

import io.github.ottermc.addon.Addon;
import io.github.ottermc.addon.Loader;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

public class Wrapper {

    public static void main(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (i == (args.length - 1))
                break;
            String key = args[i];
            if (key.startsWith("--"))
                arguments.put(key.substring(2), args[i + 1]);
            i++;
        }
        if (!arguments.containsKey("addon"))
            error("no addon specification", 40);
        if (!arguments.containsKey("gameDir"))
            error("no gameDir", 41);
        File gameDir = new File(arguments.get("gameDir"));
        Addon addon = loadAddon(arguments.get("addon"), gameDir);
        if (addon == null) {
            error("addon failed to load", 42);
            return;
        }
        File agentJar = findAgentJar(addon, gameDir);
        startGame(addon, gameDir, agentJar, args);
    }

    private static void startGame(Addon addon, File gameDir, File agentJar, String[] args) {
        String java = String.join(File.separator, System.getProperty("java.home"), "bin", "java");
        List<String> launch = new ArrayList<>();
        launch.add(java);
        System.getProperties().forEach((key, value) -> {
            launch.add(String.format("-D%s=%s", key, value));
        });
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArguments = runtimeBean.getInputArguments();
        launch.addAll(jvmArguments);
        if (System.getProperty("os.name").toLowerCase().contains("mac") && !addon.getTargetVersion().equals("1.8.9")) {
            launch.add("-XstartOnFirstThread");
        }
        launch.add("-javaagent:" + agentJar.getAbsolutePath() + "=" + addon.getName());
        launch.add("-cp");
        launch.add(String.join(File.pathSeparator, System.getProperty("java.class.path"), Loader.getClassPath(addon, gameDir)));
        launch.add(addon.getMainClass());
        launch.addAll(Arrays.asList(args));
        try {
            PrintStream out = System.out;
            PrintStream err = System.err;
            System.getProperties().forEach((key, value) -> out.printf("%s=%s\n", key, value));
            Process process = new ProcessBuilder(launch).start();
            new Thread(() -> {
                Scanner scanner = new Scanner(process.getErrorStream());
                while (scanner.hasNextLine())
                    err.println(scanner.nextLine());
            }).start();
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine())
                out.println(scanner.nextLine());
        } catch (IOException e) {
            error(e.getMessage(), -1);
        }
    }

    private static Addon loadAddon(String name, File gameDir) {
        Addon addon = Loader.loadAddonResource("/" + name + ".json");
        if (addon == null)
            return null;
        Loader.install(addon, gameDir);
        return addon;
    }

    private static File findAgentJar(Addon addon, File gameDir) {
        File agentJar = new File(gameDir, String.join(File.separator, "ottermc", String.format("client-%s.jar", addon.getClientVersion())));
        if (!agentJar.exists())
            error("failed to find client jar", 43);
        return agentJar;
    }

    private static void error(String message, int code) {
        System.err.println(message);
        System.exit(code);
    }
}
