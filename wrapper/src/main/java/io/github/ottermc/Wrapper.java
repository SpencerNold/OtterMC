package io.github.ottermc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        if (!arguments.containsKey("version") || !arguments.containsKey("gameDir"))
            System.exit(40);
        String version = arguments.get("version");
        if (!version.startsWith("ottermc-"))
            System.exit(41);
        version = version.substring(8);
        File gameDir = new File(arguments.get("gameDir"));
        File versionDir = new File(gameDir, String.join(File.separator, "versions", version));
        if (!versionDir.exists())
            System.exit(42);
        File agentJar = new File(gameDir, String.join(File.separator, "ottermc", String.format("client-v%s.jar", version)));
        if (!agentJar.exists())
            System.exit(43);

        String java = String.join(File.separator, System.getProperty("java.home"), "bin", "java");

        List<String> launch = new ArrayList<>();
        launch.add(java);
        System.getProperties().forEach((key, value) -> {
            launch.add(String.format("-D%s=%s", key, value));
        });
        launch.add("-javaagent:" + agentJar.getAbsolutePath());
        launch.add("-cp");
        launch.add(String.join(File.pathSeparator, System.getProperty("java.class.path")));
        launch.add("net.minecraft.client.main.Main");
        launch.addAll(Arrays.asList(args));

        try {
            Process process = new ProcessBuilder(launch).start();
            ExecutorService service = Executors.newFixedThreadPool(2);
            service.execute(() -> {
                Scanner scanner = new Scanner(process.getErrorStream());
                while (scanner.hasNextLine())
                    System.err.println(scanner.nextLine());
            });
            service.execute(() -> {
                Scanner scanner = new Scanner(process.getInputStream());
                while (scanner.hasNextLine())
                    System.out.println(scanner.nextLine());
            });
            PrintStream out = new PrintStream(process.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine())
                out.println(scanner.nextLine());
        } catch (IOException e) {
            System.exit(-1);
        }
    }
}
