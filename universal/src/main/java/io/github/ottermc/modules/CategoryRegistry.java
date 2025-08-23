package io.github.ottermc.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryRegistry {

    private static final Map<String, Category> CATEGORIES = new HashMap<>();

    public static void register(Category... categories) {
        for (Category c : categories)
            CATEGORIES.put(c.getDisplayName(), c);
    }

    public static void register(Iterable<Category> iterable) {
        for (Category c : iterable)
            register(c);
    }

    public static Category[] values() {
        return CATEGORIES.values().toArray(new Category[0]);
    }
}
