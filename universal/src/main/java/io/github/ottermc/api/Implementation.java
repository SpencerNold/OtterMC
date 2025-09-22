package io.github.ottermc.api;

import io.ottermc.transformer.ClassTransformer;

public interface Implementation {

    default void onPreInit(ClassTransformer transformer) {}
    default void onPostInit() {}

    void onEnable();
    void onDisable();
}
