package agent;

import agent.transformation.ClassAdapter;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
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
        File plugins = new File("ottermc" + File.separator + "plugins");
        if (plugins.exists() && plugins.isDirectory()) {
            String target = (String) main.getDeclaredField("TARGET").get(null);
            File[] files = plugins.listFiles();
            if (files != null) {
                List<String> classNames = new ArrayList<>();
                for (File f : files) {
                    try {
                        JarFile jar = new JarFile(f);
                        instrumentation.appendToSystemClassLoaderSearch(jar);
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
                        System.err.println("failed to load plugin: " + f.getName());
                    }
                }
                for (String name : classNames) {
                    Class<?> clazz = Class.forName(name, false, ClassLoader.getSystemClassLoader());
                    if (clazz.isAnnotationPresent(Plugin.class)) {
                        Plugin plugin = clazz.getAnnotation(Plugin.class);
                        if (!plugin.target().equals(target))
                            continue;
                        try {
                            Implementation implementation = (Implementation) createSafeInstance(clazz);
                            PLUGINS.put(plugin, implementation);
                        } catch (Exception ignored) {
                            System.err.printf("failed to initialize plugin: %s (%s)\n", plugin.name(), plugin.version());
                        }
                    }
                }
            }
        }
        for (Implementation implementation : PLUGINS.values())
            implementation.onPreInit(adapter);
        adapter.execute();
        adapter.clear();
        Method method = main.getDeclaredMethod("start");
        method.invoke(client);
        for (Implementation implementation : PLUGINS.values())
            implementation.onEnable();
    }

    private static Object createSafeInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            return null;
        }
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
