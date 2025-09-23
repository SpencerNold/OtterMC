package io.ottermc.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TransformerRegistry {

    @ReflectionRequired
    private final List<String> transformerRegistry = new ArrayList<>();
    @ReflectionRequired
    private final List<String> postTransformerRegistry = new ArrayList<>();

    public void register(Class<?> clazz) {
        transformerRegistry.add(clazz.getName());
    }

    public void registerPost(Class<?> clazz) {
        postTransformerRegistry.add(clazz.getName());
    }

    public void forEach(Consumer<? super Class<?>> consumer) {
        try {
            for (String name : transformerRegistry)
                consumer.accept(Class.forName(name));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void forEachPost(Consumer<? super Class<?>> consumer) {
        try {
            for (String name : postTransformerRegistry)
                consumer.accept(Class.forName(name));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
