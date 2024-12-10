package ottermc;

import java.util.HashMap;
import java.util.Map;

public class VersionRegistry {
    public static final Map<Integer, String> NAMES = new HashMap<>() {{
        put(Constants.VERSION_1_8_9, "1.8.9");
        put(Constants.VERSION_1_21_3, "1.21.3");
    }};
    public static final Map<Integer, String> JVMS = new HashMap<>() {{
       put(Constants.VERSION_1_8_9, "1.8");
       put(Constants.VERSION_1_21_3, "21");
    }};

    public static String translateVersionToNameString(int version) {
        return NAMES.get(version);
    }
}
