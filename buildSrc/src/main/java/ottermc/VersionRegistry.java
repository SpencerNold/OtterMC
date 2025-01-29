package ottermc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VersionRegistry {
    public static final Map<Integer, String> NAMES = new HashMap<>() {{
        put(Constants.VERSION_1_8_9, "1.8.9");
        put(Constants.VERSION_1_21_3, "1.21.3");
        put(Constants.VERSION_1_21_4, "1.21.4");
    }};
    public static final Map<Integer, String> JVMS = new HashMap<>() {{
       put(Constants.VERSION_1_8_9, "1.8");
        put(Constants.VERSION_1_21_3, "21");
       put(Constants.VERSION_1_21_4, "21");
    }};

    public static String translateVersionToNameString(int version) {
        return NAMES.get(version);
    }

    public static Collection<String> getVersionNames() {
        return NAMES.values();
    }
}
