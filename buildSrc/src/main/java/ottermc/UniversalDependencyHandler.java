package ottermc;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class UniversalDependencyHandler {

    private static final Predicate<String> DEPENDENCY_FILTER_PREDICATE = s -> {
        return !s.contains("launchwrapper-1.12.jar"); // TODO This will be more complex in the future...
    };

    public static List<String> filter(List<String> list) {
        return list.stream().filter(DEPENDENCY_FILTER_PREDICATE).toList();
    }
}
