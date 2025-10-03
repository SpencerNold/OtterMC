package io.ottermc.common;

import java.io.File;
import java.util.function.Consumer;

public class PluginLoader extends ReflectObject {
    public PluginLoader(ClassLoader loader, Consumer<File> consumer) throws Exception {
        super(newInstance(loader, consumer));
    }

    private static Object newInstance(ClassLoader loader, Consumer<File> consumer) throws Exception {
        Class<?> clazz = Class.forName("agent.PluginLoader", true, loader);
        return newInstance(clazz, new Class[]{Consumer.class}, new Object[]{consumer});
    }
}
