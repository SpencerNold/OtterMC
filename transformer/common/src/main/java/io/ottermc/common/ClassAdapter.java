package io.ottermc.common;

public class ClassAdapter extends ReflectObject {

    public ClassAdapter(ClassLoader loader, int maxJava, int maxASM, Class<?> nameAdapter, Class<?> methodAdapter) throws Exception {
        super(newInstance(Class.forName("me.spencernold.transformer.ClassAdapter", true, loader), new Class[]{int.class, int.class, Class.class, Class.class}, new Object[]{maxJava, maxASM, nameAdapter, methodAdapter}));
    }

    public void registerTransformerClass(Class<?> clazz) throws Exception {
        invokeMethod("registerTransformerClass", clazz);
    }

    public byte[] transform(String className, byte[] bytes) throws Exception {
        return (byte[]) invokeMethod("transform", className, bytes);
    }

    public byte[] transform(ClassLoader loader, String className, byte[] bytes) throws Exception {
        return (byte[]) invokeMethod(instance.getClass(), instance, "transform", new Class[]{ClassLoader.class, String.class, byte[].class}, new Object[]{loader, className, bytes});
    }
}
