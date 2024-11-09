package agent;

import agent.transformation.ClassAdapter;
import io.github.ottermc.Client;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class Agent {

    private static boolean injectionLoad = false;

    public static void premain(String args, Instrumentation instrumentation) {
        launch(args, instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        injectionLoad = true;
        launch(args, instrumentation);
    }

    private static void launch(String args, Instrumentation instrumentation) {
        File file = getJarFileDirectory();
        ClassAdapter adapter = new ClassAdapter(instrumentation);
        Client client = new Client(file, adapter);
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
        client.start();
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
