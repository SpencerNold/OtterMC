package io.ottermc.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class TransformerRegistry {

    private final List<Class<?>> preTransformerRegistry = new ArrayList<>();
    private final List<Class<?>> transformerRegistry = new ArrayList<>();
    private final List<Class<?>> postTransformerRegistry = new ArrayList<>();

    public void registerPre(Class<?> clazz) {
        preTransformerRegistry.add(clazz);
    }

    public void register(Class<?> clazz) {
        transformerRegistry.add(clazz);
    }

    public void registerPost(Class<?> clazz) {
        postTransformerRegistry.add(clazz);
    }

    public void forEachPre(Consumer<? super Class<?>> consumer) {
        preTransformerRegistry.forEach(consumer);
    }

    public void forEach(Consumer<? super Class<?>> consumer) {
        transformerRegistry.forEach(consumer);
    }

    public void forEachPost(Consumer<? super Class<?>> consumer) {
        postTransformerRegistry.forEach(consumer);
    }
}
