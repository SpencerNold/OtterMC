package me.spencernold.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class ClassAdapter {

    private static final int ASM_VERSION = Opcodes.ASM9;

    public byte[] transform(Class<?> transformerClass, byte[] byteCode) throws ClassTransformException {
        if (!transformerClass.isAnnotationPresent(Transformer.class))
            throw new ClassTransformException("%s does not have @Transformer annotation", transformerClass.getName());
        Transformer transformer = transformerClass.getAnnotation(Transformer.class);
        String className = transformer.className();
        try {
            className = transformer.adapter().newInstance().adapt(className);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassTransformException(e, "failed to adapt %s", className);
        }
        for (Method method : transformerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Injector.class))
                continue;
            Injector injector = method.getAnnotation(Injector.class);
            String funcName = injector.name();
            try {
                funcName = injector.adapter().newInstance().adapt(funcName);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ClassTransformException(e, "failed to adapt %s in %s", funcName, className);
            }
            // TODO Add functions and classes to be transformed to a data structure and pass to visitor
        }
        ClassReader reader = new ClassReader(byteCode);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        reader.accept(new ClassTransformVisitor(ASM_VERSION, writer), 0);
        return writer.toByteArray();
    }
}
