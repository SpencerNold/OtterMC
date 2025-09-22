package io.ottermc.transformer;

import io.ottermc.transformer.Transformable;

@FunctionalInterface
public interface TransformerInitializationHook {
    void onTransformerInitialization(Transformable transformable);
}
