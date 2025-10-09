package net.ottermc.window;

import java.util.HashMap;
import java.util.Map;

public class ArgumentReader {

    private final Map<String, String> arguments = new HashMap<>();

    public ArgumentReader(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (i == (args.length - 1))
                break;
            String key = args[i];
            if (key.startsWith("--"))
                arguments.put(key.substring(2), args[i + 1]);
            i++;
        }
    }

    public String get(String key) {
        return arguments.get(key);
    }

    public boolean has(String key) {
        return arguments.containsKey(key);
    }
}
