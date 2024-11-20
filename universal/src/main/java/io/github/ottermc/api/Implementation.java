package io.github.ottermc.api;

import agent.transformation.ClassAdapter;

public interface Implementation {

    default void onPreInit(ClassAdapter adapter) {}
    default void onPostInit() {}

    void onEnable();
    void onDisable();
}
