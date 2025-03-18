package me.spencernold.transformer;

import me.spencernold.transformer.adapters.ClassNameAdapter;
import me.spencernold.transformer.adapters.MethodNameAdapter;
import me.spencernold.transformer.objects.TransformableClassObject;
import me.spencernold.transformer.objects.TransformableMethodObject;
import me.spencernold.transformer.visitors.ClassTransformVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassAdapter {

    private final Map<String, TransformableClassObject> transformers = new HashMap<>();

    private final int asmMaxMajorVersion;
    private final Class<? extends ClassNameAdapter> defaultClassAdapter;
    private final Class<? extends MethodNameAdapter> defaultMethodAdapter;

    public ClassAdapter() {
        this(Opcodes.V23, null, null);
    }

    public ClassAdapter(int maxSupportedASMJavaVersion, Class<? extends ClassNameAdapter> defaultClassAdapter, Class<? extends MethodNameAdapter> defaultMethodAdapter) {
        this.asmMaxMajorVersion = maxSupportedASMJavaVersion;
        this.defaultClassAdapter = defaultClassAdapter;
        this.defaultMethodAdapter = defaultMethodAdapter;
    }

    public void registerTransformerClass(Class<?> clazz) throws ClassTransformException {
        if (!clazz.isAnnotationPresent(Transformer.class))
            throw new ClassTransformException("%s does not have @Transformer annotation", clazz.getName());
        Transformer transformer = clazz.getAnnotation(Transformer.class);
        String className = transformer.className();
        try {
            Class<? extends ClassNameAdapter> classNameAdapter = defaultClassAdapter == null ? transformer.adapter() : defaultClassAdapter;
            className = classNameAdapter.getDeclaredConstructor().newInstance().adapt(className);
            if (transformer.initialize())
                Class.forName(className);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ClassTransformException(e, "failed to adapt %s", className);
        } catch (ClassNotFoundException e) {
            throw new ClassTransformException(e, "class does not exist to transform %s", className);
        }
        if (!transformers.containsKey(className))
            transformers.put(className, new TransformableClassObject());
        TransformableClassObject object = transformers.get(className);
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Injector.class))
                continue;
            Injector injector = method.getAnnotation(Injector.class);
            String methodName = injector.name();
            try {
                Class<? extends MethodNameAdapter> methodNameAdapter = defaultMethodAdapter == null ? injector.adapter() : defaultMethodAdapter;
                methodName = methodNameAdapter.getDeclaredConstructor(String.class).newInstance(transformer.className()).adapt(methodName);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ClassTransformException(e, "failed to adapt %s::%s", className, methodName);
            }
            object.add(methodName, new TransformableMethodObject(clazz, methodName, method, injector.target(), injector.opcode(), injector.ordinal()));
        }
    }

    public byte[] transform(String className, byte[] byteCode) {
        TransformableClassObject object = transformers.get(className);
        if (object == null)
            return byteCode;
        int version = getMajorVersion(byteCode);
        clampMajorVersion(byteCode, 0, asmMaxMajorVersion);
        ClassReader reader = new ClassReader(byteCode);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        reader.accept(new ClassTransformVisitor(object, Opcodes.ASM9, writer), 0);
        byteCode = writer.toByteArray();
        setMajorVersion(byteCode, version);
        return byteCode;
    }

    public void clear() {
        transformers.clear();
    }

    public List<Class<?>> getClassesToTransform() {
        return transformers.keySet().stream().map(this::forNameUnsafe).collect(Collectors.toList());
    }

    private Class<?> forNameUnsafe(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void clampMajorVersion(byte[] buffer, int min, int max) {
        int version = getMajorVersion(buffer);
        if (version > max)
            version = max;
        else if (version < min)
            version = min;
        setMajorVersion(buffer, version);
    }

    private int getMajorVersion(byte[] buffer) {
        return (((buffer[6] & 0xFF) << 8) | (buffer[7] & 0xFF));
    }

    private void setMajorVersion(byte[] buffer, int version) {
        buffer[6] = (byte) ((version >> 8) & 0xFF);
        buffer[7] = (byte) (version & 0xFF);
    }
}
