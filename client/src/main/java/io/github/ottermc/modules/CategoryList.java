package io.github.ottermc.modules;

public class CategoryList {

    public static final Category WORLD = new Category("World");
    public static final Category VISUAL = new Category("Visual");
    public static final Category DISPLAY = new Category("Display");
    public static final Category UTILITY = new Category("Utility");


    public static Category[] values() {
        return new Category[] { WORLD, VISUAL, DISPLAY, UTILITY };
    }
}
