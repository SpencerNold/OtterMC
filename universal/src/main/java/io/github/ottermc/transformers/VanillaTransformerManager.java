package io.github.ottermc.transformers;

import io.github.ottermc.ClassTransformer;
import io.github.ottermc.logging.Logger;
import io.ottermc.transformer.Transformable;
import io.ottermc.transformer.TransformableManager;
import io.ottermc.transformer.TransformerRegistry;

public class VanillaTransformerManager implements TransformableManager<Transformable> {

    private final TransformerRegistry registry;

    public VanillaTransformerManager(TransformerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void executePreTransform(Transformable instance) {
        if (!(instance instanceof ClassTransformer)) {
            Logger.error("Attempting to Vanilla transform, but provided with: " + instance.getClass().getName());
            return;
        }
        ClassTransformer transformer = (ClassTransformer) instance;
        try {
            registry.forEachPre(instance::register);
            transformer.execute();
            transformer.clear();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @Override
    public void executeInitialTransform(Transformable instance) {
        if (!(instance instanceof ClassTransformer)) {
            Logger.error("Attempting to Vanilla transform, but provided with: " + instance.getClass().getName());
            return;
        }
        ClassTransformer transformer = (ClassTransformer) instance;
        try {
            registry.forEach(instance::register);
            transformer.execute();
            transformer.clear();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @Override
    public void executePostTransform(Transformable instance) {
        if (!(instance instanceof ClassTransformer)) {
            Logger.error("Attempting to Vanilla transform, but provided with: " + instance.getClass().getName());
            return;
        }
        ClassTransformer transformer = (ClassTransformer) instance;
        try {
            registry.forEachPost(instance::register);
            transformer.execute();
            transformer.clear();
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
