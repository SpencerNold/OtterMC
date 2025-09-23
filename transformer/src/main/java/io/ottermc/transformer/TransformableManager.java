package io.ottermc.transformer;

public interface TransformableManager<T extends Transformable> {
    void executePreTransform(T instance);
    void executeInitialTransform(T instance);
    void executePostTransform(T instance);
}
