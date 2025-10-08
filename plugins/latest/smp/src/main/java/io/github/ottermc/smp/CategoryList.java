package io.github.ottermc.smp;

import io.github.ottermc.modules.Category;

public class CategoryList {

    public static final Category CLIENT = new Category("Client");
    public static final Category GAME = new Category("Game");
    public static final Category WORLD = new Category("World");
    public static final Category MOVEMENT = new Category("Movement");

    public static Category[] values() {
        return new Category[] { CLIENT, GAME, WORLD, MOVEMENT };
    }
}
