package ottermc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VersionRegistry {
    public static final Map<Integer, String> NAMES = new HashMap<>() {{
        put(Constants.VERSION_1_8_9, "v1.8.9");
        put(Constants.VERSION_LATEST, "latest");
    }};
    public static final Map<Integer, String> JVMS = new HashMap<>() {{
       put(Constants.VERSION_1_8_9, "1.8");
       put(Constants.VERSION_LATEST, "21");
    }};

    public static String translateVersionToNameString(int version) {
        return NAMES.get(version);
    }

    public static String translateVersionToMinecraftString(int version) {
        if (version == Constants.VERSION_LATEST)
            return LatestVersion.NAME;
        return NAMES.get(version).substring(1);
    }

    public static Collection<String> getVersionNames() {
        return NAMES.values();
    }
}
