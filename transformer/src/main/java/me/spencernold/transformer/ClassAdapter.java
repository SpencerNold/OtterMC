package me.spencernold.transformer;

import java.lang.reflect.Method;

public class ClassAdapter {

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

        }
        // TODO transform
        return byteCode;
    }
}
