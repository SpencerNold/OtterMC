package io.ottermc.fml189;

import io.ottermc.transformer.TransformerRegistry;

public class FMLTransformerManager {

    private final TransformerRegistry registry;

    public FMLTransformerManager(TransformerRegistry registry) {
        this.registry = registry;
    }

    public TransformerRegistry getRegistry() {
        return registry;
    }
}
