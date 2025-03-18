package io.github.ottermc.api;

import agent.ClassTransformer;

public interface Implementation {

    default void onPreInit(ClassTransformer transformer) {}
    default void onPostInit() {}

    void onEnable();
    void onDisable();
}
