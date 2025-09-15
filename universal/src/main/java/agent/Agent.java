package agent;

import agent.adapters.MinecraftClassNameAdapter;
import agent.adapters.MinecraftFieldNameAdapter;
import agent.adapters.MinecraftMethodNameAdapter;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Initializer;
import io.github.ottermc.api.Plugin;
import io.github.ottermc.c2.ServerController;
import io.github.ottermc.logging.Logger;
import me.spencernold.transformer.Reflection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Agent {

    public static final Map<Plugin, Implementation> PLUGINS = new HashMap<>();

    private static State state = State.BOOT;
    private static boolean injectionLoad = false;
    private static Initializer client;

    public static void premain(String args, Instrumentation instrumentation) {
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            e.printStackTrace(Logger.getLoggerErrorStream());
        }
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        injectionLoad = true;
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            e.printStackTrace(Logger.getLoggerErrorStream());
        }
    }

    private static void launch(String args, Instrumentation instrumentation) throws Exception {
        // Setup KWAF
        // TODO
        // Setup BTCLib
        Reflection reflection = new Reflection(MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class, MinecraftFieldNameAdapter.class);
        Reflection.setSystemReflectClass(reflection);
        // Start
        File file = getJarFile();
        if (file == null)
            return;
        File dir = file.getParentFile();
        int version = getCompiledJarMajorVersion();
        ClassTransformer transformer = new ClassTransformer(instrumentation);
        Class<?> main = Class.forName("io.github.ottermc.Client");
        Constructor<?> constructor = main.getDeclaredConstructor(File.class, ClassTransformer.class);
        client = (Initializer) constructor.newInstance(dir, transformer);
        Agent.setState(State.START);
        File plugins = new File("ottermc" + File.separator + "plugins");
        if (plugins.exists() && plugins.isDirectory()) {
            String target = (String) main.getDeclaredField("TARGET").get(null);
            File[] files = plugins.listFiles();
            if (files != null) {
                List<String> classNames = new ArrayList<>();
                for (File f : files) {
                    if (!f.getName().endsWith(".jar"))
                        continue;
                    try {
                        JarFile jar = new JarFile(f);
                        instrumentation.appendToSystemClassLoaderSearch(jar);
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.endsWith(".class")) {
                                int ver = getMajorVersion(jar, entry);
                                if (ver > version)
                                    continue;
                                name = name.replace('/', '.').substring(0, name.length() - 6);
                                classNames.add(name);
                            }
                        }
                        jar.close();
                    } catch (IOException ignored) {
                        Logger.error("failed to load plugin: " + f.getName());
                    }
                }
                for (String name : classNames) {
                    Class<?> clazz = findClassOrNullUnloaded(name);
                    if (clazz == null)
                        continue;
                    if (clazz.isAnnotationPresent(Plugin.class)) {
                        Plugin plugin = clazz.getAnnotation(Plugin.class);
                        if (!plugin.target().equals(target))
                            continue;
                        try {
                            Implementation implementation = (Implementation) createSafeInstance(clazz);
                            PLUGINS.put(plugin, implementation);
                            Logger.log("loading plugin: " + plugin.name());
                        } catch (Exception ignored) {
                            Logger.errorf("failed to initialize plugin: %s (%s)\n", plugin.name(), plugin.version());
                        }
                    }
                }
            }
        }
        if (PLUGINS.isEmpty())
            Logger.log("Running vanilla client version, no plugins are installed!");
        setState(State.PRE_INIT);
        for (Implementation implementation : PLUGINS.values())
            implementation.onPreInit(transformer);
        transformer.execute();
        transformer.clear();
        setState(State.INIT);
        client.start();
        for (Implementation implementation : PLUGINS.values())
            implementation.onEnable();
        ServerController.start();
        setState(State.POST_INIT);
    }

    private static int getCompiledJarMajorVersion() throws IOException {
        File file = getJarFile();
        if (file == null)
            throw new IOException();
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();
        int version = -1;
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                version = getMajorVersion(jar, entry);
                break;
            }
        }
        jar.close();
        return version;
    }

    private static int getMajorVersion(JarFile jar, JarEntry entry) throws IOException {
        if (entry.getSize() < 8)
            return -1;
        InputStream input = jar.getInputStream(entry);
        byte[] bytes = new byte[8];
        if (input.read(bytes) != 8)
            return -1;
        return (((bytes[6] & 0xFF) << 8) | (bytes[7] & 0xFF));
    }

    private static Class<?> findClassOrNullUnloaded(String name) {
        try {
            return Class.forName(name, false, ClassLoader.getSystemClassLoader());
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

    private static File getJarFile() {
        try {
            return new File(Agent.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace(Logger.getLoggerErrorStream());
            return null;
        }
    }

    public static boolean isInjectionLoad() {
        return injectionLoad;
    }

    public static State getState() {
        return state;
    }

    public static void setState(State state) {
        Agent.state = state;
    }

    public static Initializer getClient() {
        return client;
    }
}
