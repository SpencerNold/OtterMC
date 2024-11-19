package ottermc;

import java.util.HashMap;
import java.util.Map;

public class VersionRegistry {
    public static final Map<Integer, String> NAMES = new HashMap<>() {{
        put(Constants.VERSION_1_8_9, "1.8.9");
        put(Constants.VERSION_1_21_3, "latest");
    }};
    public static final Map<Integer, String> JVMS = new HashMap<>() {{
       put(Constants.VERSION_1_8_9, "1.8");
       put(Constants.VERSION_1_21_3, "21");
    }};
    public static final String LATEST_MINECRAFT_VERSION = "1.21.3";

    public static String translateVersionToNameString(int version) {
        if (version == Constants.VERSION_1_21_3)
            return LATEST_MINECRAFT_VERSION;
        return NAMES.get(version);
    }
}
