package net.ottermc.window;

import net.ottermc.window.versions.VanillaVersion;
import net.ottermc.window.versions.Version;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class Main {

    public static final Version VERSION189 = new VanillaVersion(
            "1.8.9",
            "8",
            new String[]{},
            "vanilla-1.8.9.jar"
    );
    public static final Version VERSION12110 = new VanillaVersion(
            "1.21.10",
            "21",
            new String[]{},
            "vanilla-1.21.10.jar"
    );

    public static String[] flags;
    public static String[] properties;
    public static String[] classpath;
    public static String[] arguments;
    public static File gameDir;

    public static void main(String[] args) {
        ArgumentReader reader = new ArgumentReader(args);
        flags = decodeArguments(reader.get("jvm"));
        properties = decodeArguments(reader.get("properties"));
        classpath = decodeArguments(reader.get("classpath"));
        arguments = decodeArguments(reader.get("arguments"));
        reader = new ArgumentReader(arguments);
        if (!reader.has("gameDir"))
            Logger.error("no gameDir", 42);
        Main.gameDir = new File(reader.get("gameDir"));
        // TODO Will pretty up later
        Window.create(Arrays.asList(VERSION189, VERSION12110));
    }

    private static String[] decodeArguments(String s) {
        return new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).split(",");
    }
}
