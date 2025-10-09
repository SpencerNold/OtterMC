package io.ottermc.transformer;

import io.github.ottermc.io.InputStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AgentClassLoader extends ClassLoader {

    private final List<JarFile> jars = new ArrayList<>();
    private final Map<String, Class<?>> loadedClasses = new HashMap<>();

    public void append(JarFile jar) {
        jars.add(jar);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (loadedClasses.containsKey(name))
            return loadedClasses.get(name);
        String canonical = name.replace('.', '/') + ".class";
        for (JarFile jar : jars) {
            JarEntry entry = jar.getJarEntry(canonical);
            if (entry == null)
                continue;
            try {
                InputStream input = jar.getInputStream(entry);
                byte[] bytes = InputStreams.readAllBytes(input);
                Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
                loadedClasses.put(name, clazz);
                return clazz;
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            }
        }
        throw new ClassNotFoundException(name);
    }
}
