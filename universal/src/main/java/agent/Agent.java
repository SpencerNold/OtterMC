package agent;

import agent.transformation.ClassAdapter;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class Agent {

    public static final Map<Plugin, Implementation> PLUGINS = new HashMap<>();

    private static boolean injectionLoad = false;

    public static void premain(String args, Instrumentation instrumentation) {
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        injectionLoad = true;
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void launch(String args, Instrumentation instrumentation) throws Exception {
        File file = getJarFileDirectory();
        ClassAdapter adapter = new ClassAdapter(instrumentation);
        Class<?> main = Class.forName("io.github.ottermc.Client");
        Constructor<?> constructor = main.getDeclaredConstructor(File.class, ClassAdapter.class);
        Object client = constructor.newInstance(file, adapter);
        File plugins = new File("plugins");
        List<Implementation> registeredPluginClasses = new ArrayList<>();
        if (plugins.exists() && plugins.isDirectory()) {
            File[] files = plugins.listFiles();
            if (files != null) {
                for (File f : files) {
                    try {
                        JarFile jar = new JarFile(f);
                        instrumentation.appendToSystemClassLoaderSearch(jar);
                        jar.close();
                    } catch (IOException ignored) {
                        System.err.println("failed to load plugin: " + f.getName());
                    }
                }
                Class<?>[] classes = instrumentation.getAllLoadedClasses();
                for (Class<?> clazz : classes) {
                    if (clazz.isAnnotationPresent(Plugin.class)) {
                        Plugin plugin = clazz.getAnnotation(Plugin.class);
                        try {
                            // I know newInstance is deprecated, but this code should always be compiled in Java8
                            // I can and will swap it to the constructor newInstance method when I flush out
                            // the reflection utility
                            Implementation implementation = (Implementation) clazz.newInstance();
                            registeredPluginClasses.add(implementation);
                            PLUGINS.put(plugin, implementation);
                        } catch (Exception ignored) {
                            System.err.printf("failed to initialize plugin: %s (%s)\n", plugin.name(), plugin.version());
                        }
                    }
                }
            }
        }
        for (Implementation implementation : registeredPluginClasses)
            implementation.onPreInit(adapter);
        try {
            adapter.execute();
        } catch (UnmodifiableClassException e) {
            throw new RuntimeException(e);
        }
        adapter.clear();
        Method method = main.getDeclaredMethod("start");
        method.invoke(client);
        for (Implementation implementation : registeredPluginClasses)
            implementation.onEnable();
    }

    private static File getJarFileDirectory() {
        try {
            return new File(Agent.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInjectionLoad() {
        return injectionLoad;
    }
}
