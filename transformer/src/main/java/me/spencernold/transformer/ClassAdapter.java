package me.spencernold.transformer;

import me.spencernold.transformer.objects.TransformableClassObject;
import me.spencernold.transformer.objects.TransformableMethodObject;
import me.spencernold.transformer.visitors.ClassTransformVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassAdapter {

    private static final int ASM_VERSION = Opcodes.ASM9;

    private final Map<String, TransformableClassObject> transformers = new HashMap<>();
    private final int asmMaxMajorVersion;

    public ClassAdapter(int maxSupportedASMJavaVersion) {
        this.asmMaxMajorVersion = maxSupportedASMJavaVersion;
    }

    public void registerTransformerClass(Class<?> clazz) throws ClassTransformException {
        if (!clazz.isAnnotationPresent(Transformer.class))
            throw new ClassTransformException("%s does not have @Transformer annotation", clazz.getName());
        Transformer transformer = clazz.getAnnotation(Transformer.class);
        String className = transformer.className();
        try {
            className = transformer.adapter().newInstance().adapt(className);
            if (transformer.initialize())
                Class.forName(className);
        } catch (InstantiationException | IllegalAccessException e) {
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
                methodName = injector.adapter().newInstance().adapt(methodName);
            } catch (InstantiationException | IllegalAccessException e) {
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
        //setMajorVersion(byteCode, version);
        return byteCode;
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
