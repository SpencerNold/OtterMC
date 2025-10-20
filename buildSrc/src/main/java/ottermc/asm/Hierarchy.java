package ottermc.asm;

import org.objectweb.asm.ClassReader;
import ottermc.structures.Pair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Hierarchy {

    private static final Map<String, byte[]> classes = new HashMap<>();
    private static final Map<String, Pair<Boolean, List<String>>> classToParentMap = new HashMap<>();

    public static List<String> getParentsOfClass(String name) {
        return classToParentMap.getOrDefault(name, new Pair<>(false, new ArrayList<>())).getValue();
    }

    public static boolean isClientClass(String name) {
        return classToParentMap.getOrDefault(name, new Pair<>(false, new ArrayList<>())).getKey();
    }

    public static void create(File file, boolean client) throws IOException {
        List<String> classes = readAllClasses(file);
        for (String name : classes) {
            List<String> parents = getParentClassesRecursive(name);
            classToParentMap.put(name, new Pair<>(client, parents));
        }
    }

    public static void finish() {
        classes.clear();
    }

    private static List<String> readAllClasses(File file) throws IOException {
        List<String> classes = new ArrayList<>();
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (entry.isDirectory() || !name.endsWith(".class"))
                continue;
            name = name.substring(0, name.length() - 6);
            InputStream input = jar.getInputStream(entry);
            byte[] bytes = input.readAllBytes();
            Hierarchy.classes.put(name, bytes);
            classes.add(name);
        }
        jar.close();
        return classes;
    }

    private static List<String> getParentClassesRecursive(String name) throws IOException {
        if (name == null)
            return new ArrayList<>(); // ???
        if (name.equals("java/lang/Object"))
            return List.of("java/lang/Object");
        byte[] bytes = getFromName(name);
        if (bytes == null)
            return new ArrayList<>();
        List<String> classes = new ArrayList<>();
        classes.add(name);
        ClassReader reader = new ClassReader(bytes);
        classes.addAll(getParentClassesRecursive(reader.getSuperName()));
        for (String s : reader.getInterfaces())
            classes.addAll(getParentClassesRecursive(s));
        return classes;
    }

    private static byte[] getFromName(String name) throws IOException {
        byte[] bytes = classes.get(name);
        if (bytes == null) {
            InputStream input = Hierarchy.class.getClassLoader().getResourceAsStream(name + ".class");
            if (input == null)
                return null;
            bytes = input.readAllBytes();
            input.close();
        }
        return bytes;
    }
}
