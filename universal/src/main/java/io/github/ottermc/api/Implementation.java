package io.github.ottermc.api;

public interface Implementation {

    default void onPreInit(ClassAdapter1 adapter) {}
    default void onPostInit() {}

    void onEnable();
    void onDisable();
}
