package agent;

import agent.transformation.ClassAdapter;
import io.github.ottermc.api.Implementation;
import io.github.ottermc.api.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            e.printStackTrace();
        }
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        injectionLoad = true;
        try {
            launch(args, instrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void launch(String args, Instrumentation instrumentation) throws Exception {
        File file = getJarFile();
        if (file == null)
            return;
        File dir = file.getParentFile();
        int version = getCompiledJarMajorVersion();
        ClassAdapter adapter = new ClassAdapter(instrumentation);
        Class<?> main = Class.forName("io.github.ottermc.Client");
        Constructor<?> constructor = main.getDeclaredConstructor(File.class, ClassAdapter.class);
        Object client = constructor.newInstance(dir, adapter);
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

    private static int getCompiledJarMajorVersion() throws IOException {
        JarFile jar = new JarFile(getJarFile());
        Enumeration<JarEntry> entries = jar.entries();
        int version = -1;
        if (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            version = getMajorVersion(jar, entry);
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
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isInjectionLoad() {
        return injectionLoad;
    }
}
