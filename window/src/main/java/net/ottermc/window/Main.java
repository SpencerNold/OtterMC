package net.ottermc.window;

import net.ottermc.window.versions.VanillaVersion;
import net.ottermc.window.versions.Version;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    public static final Version VERSION189 = new VanillaVersion(
            "Client 1.8.9",
            "1.8.9",
            "8",
            new String[]{},
            "vanilla-1.8.9.jar"
    );
    public static final Version VERSION12110 = new VanillaVersion(
            "Client 1.21.10",
            "1.21.10",
            "21",
            new String[]{"-XstartOnFirstThread"},
            "vanilla-1.21.10.jar"
    );

    public static String[] flags;
    public static String[] arguments;
    public static File gameDir;
    public static String username;

    public static void main(String[] args) {
        if (args.length == 0) {
            Window.create(Arrays.asList(VERSION189, VERSION12110));
            return;
        }
        ArgumentReader reader = new ArgumentReader(args);
        flags = decodeArguments(reader.get("jvm"));
        arguments = decodeArguments(reader.get("arguments"));
        reader = new ArgumentReader(arguments);
        if (!reader.has("gameDir"))
            Logger.error("no gameDir", 42);
        Main.gameDir = new File(reader.get("gameDir"));
        Main.username = reader.has("username") ? reader.get("username") : "Player";
        Window.create(Arrays.asList(VERSION189, VERSION12110));
    }

    private static String[] decodeArguments(String s) {
        return new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).split(",");
    }

    public static void dumpArguments() {
        // Make sure this is never used in production, it puts the client's access token into the logs,
        // and end users may copy and paste it into the internet somewhere
        System.out.println("===== Flags =====");
        for (String s : flags)
            System.out.println(s);
        System.out.println("\n\n");
        System.out.println("===== Arguments =====");
        for (String s : arguments)
            System.out.println(s);
        System.out.println("\n\n");
    }
}
