package io.github.ottermc.api;

import agent.transformation.ClassAdapter;

public interface Implementation {

    default void onPreInit(ClassAdapter adapter) {}

    void onEnable();
    void onDisable();

}
