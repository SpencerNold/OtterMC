package io.github.ottermc.api;

import io.ottermc.transformer.TransformerRegistry;

public interface Implementation {

    default void onPreInit(TransformerRegistry registry) {}
    default void onPostInit() {}

    void onEnable();
    void onDisable();
}
