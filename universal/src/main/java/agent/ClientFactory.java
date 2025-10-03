package agent;

import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.c2.ServerController;
import io.github.ottermc.logging.Logger;
import io.ottermc.transformer.State;
import io.ottermc.transformer.TransformerRegistry;
import io.ottermc.transformer.adapters.MinecraftClassNameAdapter;
import io.ottermc.transformer.adapters.MinecraftFieldNameAdapter;
import io.ottermc.transformer.adapters.MinecraftMethodNameAdapter;
import io.ottermc.transformer.io.InputStreams;
import me.spencernold.transformer.Reflection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

public class ClientFactory {

    private String[] pluginNames = new String[0];
    private PluginLoader pluginLoader;
    private ClassLoader classLoader;

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
        Map<Plugin, Implementation> pluginMap = new HashMap<>();
        try {
            // Setup KWAF
            me.spencernold.kwaf.logger.Logger.Companion.setSystemLogger(Logger.KWAF_LOGGER_IMPLEMENTATION);
            // Setup BTCLib
            me.spencernold.transformer.Logger.instance = Logger.BTCLIB_LOGGER_IMPLEMENTATION;
            Reflection reflection = new Reflection(MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class, MinecraftFieldNameAdapter.class);
            Reflection.setSystemReflectClass(reflection);
            // Start Client
            File file = getPathOfJar();
            File dir = file.getParentFile();
            TransformerRegistry registry = new TransformerRegistry();

            Class<?> main = Class.forName("io.github.ottermc.Client");
            Constructor<?> constructor = main.getDeclaredConstructor(File.class, TransformerRegistry.class);
            Initializer client = (Initializer) constructor.newInstance(dir, registry);
            StateRegistry.setState(State.START);
            File plugins = new File("ottermc" + File.separator + "plugins");
            if (plugins.exists() && plugins.isDirectory()) {
                String target = (String) main.getDeclaredField("TARGET").get(null);
                for (String pluginName : pluginNames) {
                    File pluginFile = new File(plugins, pluginName + ".jar");
                    if (!pluginFile.exists()) {
                        Logger.log("unable to find plugin: " + pluginName + ", ignoring and proceeding");
                        continue;
                    }
                    try {
                        if (pluginLoader == null) {
                            Logger.error("missing PluginClassLoader, skipping plugins");
                            break;
                        }
                        pluginLoader.load(pluginFile);
                        JarFile jar = new JarFile(pluginFile);
                        ZipEntry entry = jar.getEntry("MainClassManifest.txt");
                        InputStream input = jar.getInputStream(entry);
                        String className = new String(InputStreams.readAllBytes(input), StandardCharsets.UTF_8);
                        input.close();
                        jar.close();
                        Class<?> clazz = findClassOrNullUnloaded(className);
                        if (clazz == null) {
                            Logger.error("failed find main class of: " + className + " in " + pluginName);
                            continue;
                        }
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
                    } catch (IOException ignored) {
                        Logger.error("failed to load plugin: " + pluginName);
                    }
                }
                if (pluginMap.isEmpty())
                    Logger.log("running vanilla client version, no plugins are installed");
                StateRegistry.setState(State.PRE_INIT);
                for (Implementation implementation : pluginMap.values())
                    implementation.onPreInit(registry);
            }
            ServerController.start();
            return new Client(registry, client, pluginMap);
        } catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }

    private File getPathOfJar() throws URISyntaxException {
        URI uri = ClientFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        String value = String.valueOf(uri);
        Pattern pattern = Pattern.compile("jar:(.+?)!.*$");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches())
            return new File(URI.create(matcher.group(1)));
        return new File(uri);
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
}
