package io.ottermc.common;

import java.util.List;

public class TransformerRegistry extends ReflectObject {

    TransformerRegistry(Object instance) {
        super(instance);
    }

    @SuppressWarnings("unchecked")
    public List<Class<?>> getTransformerRegistry() throws Exception {
        return (List<Class<?>>) getField("transformerRegistry");
    }

    @SuppressWarnings("unchecked")
    public List<Class<?>> getPostTransformerRegistry() throws Exception {
        return (List<Class<?>>) getField("postTransformerRegistry");
    }
}
