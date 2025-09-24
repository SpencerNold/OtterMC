package agent;

import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.logging.Logger;
import io.ottermc.transformer.State;
import io.ottermc.transformer.TransformerRegistry;
import io.ottermc.transformer.adapters.MinecraftClassNameAdapter;
import io.ottermc.transformer.adapters.MinecraftFieldNameAdapter;
import io.ottermc.transformer.adapters.MinecraftMethodNameAdapter;
import me.spencernold.transformer.Reflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClientFactory {

    private String[] pluginNames = new String[0];
    private PluginLoader pluginLoader = null;
    private ClassLoader classLoader = null;

    public ClientFactory setPlugins(String[] plugins) {
        this.pluginNames = plugins;
        return this;
    }

    public ClientFactory setPluginLoader(PluginLoader loader) {
        this.pluginLoader = loader;
        return this;
    }

    public ClientFactory setClassLoader(ClassLoader loader) {
        this.classLoader = loader;
        return this;
    }

    public Client create() {
        Map<Plugin, Implementation> pluginMap;
        try {
            // Setup KWAF
            me.spencernold.kwaf.logger.Logger.Companion.setSystemLogger(Logger.KWAF_LOGGER_IMPLEMENTATION);
            // Setup BTCLib
            me.spencernold.transformer.Logger.instance = Logger.BTCLIB_LOGGER_IMPLEMENTATION;
            Reflection reflection = new Reflection(MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class, MinecraftFieldNameAdapter.class);
            Reflection.setSystemReflectClass(reflection);
            // Start Client
            File file = new File(ClientFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File dir = file.getParentFile();
            TransformerRegistry registry = new TransformerRegistry();
            Class<?> main = Class.forName("io.github.ottermc.Client");
            Constructor<?> constructor = main.getDeclaredConstructor(File.class, TransformerRegistry.class);
            Initializer client = (Initializer) constructor.newInstance(dir, registry);
            pluginMap = new HashMap<>();
            StateRegistry.setState(State.START);
            File plugins = new File("ottermc" + File.separator + "plugins");
            if (plugins.exists() && plugins.isDirectory()) {
                String target = (String) main.getDeclaredField("TARGET").get(null);
                List<String> classNames = new ArrayList<>();
                for (String pluginName : pluginNames) {
                    File plugin = new File(plugins, pluginName + ".jar");
                    if (!plugin.exists()) {
                        Logger.log("unable to find plugin: " + pluginName + ", ignoring and proceeding");
                        continue;
                    }
                    try {
                        if (pluginLoader == null) {
                            Logger.error("missing PluginClassLoader, skipping plugins");
                            break;
                        }
                        pluginLoader.load(plugin);
                        JarFile jar = new JarFile(plugin);
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.endsWith(".class")) {
                                name = name.replace('/', '.').substring(0, name.length() - 6);
                                classNames.add(name);
                            }
                        }
                        jar.close();
                    } catch (IOException ignored) {
                        Logger.error("failed to load plugin: " + pluginName);
                    }
                }
                for (String name : classNames) {
                    Class<?> clazz = findClassOrNullUnloaded(name);
                    if (clazz == null)
                        continue;
                    if (clazz.isAnnotationPresent(Plugin.class)) {
                        Plugin plugin = clazz.getAnnotation(Plugin.class);
                        if (!plugin.target().equals(target)) {
                            Logger.errorf("skipping %s (%s) as the client version is %s", plugin.name(), plugin.target(), target);
                            continue;
                        }
                        Implementation implementation = (Implementation) createSafeInstance(clazz);
                        pluginMap.put(plugin, implementation);
                        Logger.log("loading plugin: " + plugin.name());
                    }
                }
                if (pluginMap.isEmpty())
                    Logger.log("running vanilla client version, no plugins are installed");
                StateRegistry.setState(State.PRE_INIT);
                for (Implementation implementation : pluginMap.values())
                    implementation.onPreInit(registry);
            }
            return new Client(registry, client, pluginMap);
        } catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }

    private Class<?> findClassOrNullUnloaded(String name) {
        try {
            return Class.forName(name, false, classLoader);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return null;
        }
    }

    private static Object createSafeInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static final class Client {

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
}
