package io.github.ottermc.modules;

public class CategoryList {

    public static final Category VISUAL = new Category("Visual");
    public static final Category DISPLAY = new Category("Display");
    public static final Category GAME = new Category("Game");
    public static final Category ONLINE = new Category("Online");
    public static final Category ANALYTICAL = new Category("Analytical");

    public static Category[] values() {
        return new Category[] { VISUAL, DISPLAY, GAME, ONLINE, ANALYTICAL };
    }
}
