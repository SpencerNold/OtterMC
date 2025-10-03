package io.ottermc.common;

public class ClientFactory extends ReflectObject {

    public ClientFactory(ClassLoader loader) throws Exception {
        super(newInstance(loader, "agent.ClientFactory"));
    }

    public ClientFactory setPlugins(String[] plugins) throws Exception {
        invokeMethod("setPlugins", (Object) plugins);
        return this;
    }

    public ClientFactory setPluginLoader(PluginLoader pluginLoader) throws Exception {
        invokeMethod("setPluginLoader", pluginLoader.instance);
        return this;
    }

    public ClientFactory setClassLoader(ClassLoader classLoader) throws Exception {
        invokeMethod(instance.getClass(), instance, "setClassLoader", new Class[]{ClassLoader.class}, new Object[]{classLoader});
        return this;
    }

    public Client create() throws Exception {
        Object client = invokeMethod("create");
        return new Client(client);
    }
}
