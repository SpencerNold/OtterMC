package io.github.ottermc;

import java.util.List;

public interface Patch {
    List<Pair<Class<?>, Object>> getOverrides();
}
