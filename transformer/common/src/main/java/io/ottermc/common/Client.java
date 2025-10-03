package io.ottermc.common;

import java.util.Map;

public class Client extends ReflectObject {

    Client(Object instance) {
        super(instance);
    }

    public TransformerRegistry getRegistry() throws Exception {
        Object registry = invokeMethod("getRegistry");
        return new TransformerRegistry(registry);
    }

    public Initializer getClient() throws Exception {
        Object client = invokeMethod("getClient");
        return new Initializer(client);
    }

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getPluginMap() throws Exception {
        return (Map<Object, Object>) invokeMethod("getPluginMap");
    }
}
