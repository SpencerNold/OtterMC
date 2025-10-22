package net.ottermc.window;

import net.ottermc.window.versions.impl.FabricVersion;
import net.ottermc.window.versions.impl.VanillaVersion;
import net.ottermc.window.versions.Version;
import net.ottermc.window.versions.VersionLoader;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Main {

    public static String[] flags;
    public static String[] arguments;
    public static File gameDir;
    public static String username;
    public static final List<Version> versions = new ArrayList<>();

    public static void main(String[] args) {
        ArgumentReader reader = new ArgumentReader(args);
        flags = decodeArguments(reader.get("jvm"));
        arguments = decodeArguments(reader.get("arguments"));
        reader = new ArgumentReader(arguments);
        if (!reader.has("gameDir"))
            Logger.error("no gameDir", 42);
        Main.gameDir = new File(reader.get("gameDir"));
        Main.username = reader.has("username") ? reader.get("username") : "Player";
        VersionLoader loader = new VersionLoader();
        loader.register("vanilla", VanillaVersion.class);
        loader.register("fabric", FabricVersion.class);
        loader.loadManifest();
        Window.create();
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
