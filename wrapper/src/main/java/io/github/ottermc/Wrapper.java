package io.github.ottermc;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
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
        if (!arguments.containsKey("gameDir"))
            error("no gameDir", 42);
        File gameDir = new File(arguments.get("gameDir"));
        try {
            launchWindow(gameDir, args);
        } catch (IOException | InterruptedException e) {
            error(e.getMessage(), -1);
        }
    }

    private static void launchWindow(File gameDir, String[] args) throws IOException, InterruptedException {
        List<String> arguments = new ArrayList<>();
        arguments.add(getGameFile(gameDir, "jre", "8", "bin", "java"));
        arguments.add("-cp");
        arguments.add(getGameFile(gameDir, "ottermc", "window.jar"));
        arguments.add("net.ottermc.window.Main");

        arguments.add("--jvm");
        arguments.add(encodeArguments(getVirtualMachineArguments()));
        arguments.add("--properties");
        arguments.add(encodeArguments(getJavaArguments()));
        arguments.add("--classpath");
        arguments.add(encodeArguments(System.getProperty("java.class.path")));
        arguments.add("--arguments");
        arguments.add(encodeArguments(args));

        ProcessBuilder builder = new ProcessBuilder(arguments).inheritIO();
        Process process = builder.start();
        int code = process.waitFor();
        System.exit(code);
    }

    private static String[] getJavaArguments() {
        List<String> list = new ArrayList<>();
        System.getProperties().forEach((key, value) -> {
            list.add(String.format("-D%s=%s", key, value));
        });
        return list.toArray(new String[0]);
    }

    private static String[] getVirtualMachineArguments() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArguments = runtimeBean.getInputArguments();
        return jvmArguments.toArray(new String[0]);
    }

    private static String encodeArguments(String... arguments) {
        return Base64.getEncoder().encodeToString(String.join(",", arguments).getBytes(StandardCharsets.UTF_8));
    }

    private static String getGameFile(File gameDir, String... path) {
        return new File(gameDir, String.join(File.separator, path)).getAbsolutePath();
    }

    private static void error(String message, int code) {
        System.err.println(message);
        System.exit(code);
    }
}
