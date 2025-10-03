package agent;

import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.api.Plugin;
import io.ottermc.transformer.TransformerRegistry;

import java.util.Map;

public final class Client {

    private static Client instance;

    private final TransformerRegistry registry;
    private final Initializer client;
    private final Map<Plugin, Implementation> pluginMap;

    public Client(TransformerRegistry registry, Initializer client, Map<Plugin, Implementation> pluginMap) {
        this.registry = registry;
        this.client = client;
        this.pluginMap = pluginMap;
        instance = this;
    }

    public TransformerRegistry getRegistry() {
        return registry;
    }

    public Initializer getClient() {
        return client;
    }

    public Map<Plugin, Implementation> getPluginMap() {
        return pluginMap;
    }

    public static Client getInstance() {
        return instance;
    }
}
