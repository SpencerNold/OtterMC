package io.ottermc.fml189;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OMCTransformer implements IClassTransformer {

    private Object adapter;

    private void init() {
        try {
            final int v1_8 = 52;
            final int asm5 = 327680;
            Class<?> classNameAdapterClass = Class.forName("io.ottermc.transformer.adapters.MinecraftClassNameAdapter", true, Launch.classLoader);
            Class<?> methodNameAdapterClass = Class.forName("io.ottermc.transformer.adapters.MinecraftMethodNameAdapter", true, Launch.classLoader);

            Class<?> clazz = Class.forName("me.spencernold.transformer.ClassAdapter", true, Launch.classLoader);
            Constructor<?> constructor = clazz.getDeclaredConstructor(int.class, int.class, Class.class, Class.class);
            adapter = constructor.newInstance(v1_8, asm5, classNameAdapterClass, methodNameAdapterClass);

            //Class<?> agent = Class.forName("agent.Agent", false, loader);
            //Object registry = agent.getMethod("getTransformerRegistry").invoke(null);

            //List<String> transformerRegistry = getTransformers(registry, "transformerRegistry");
            //List<String> postTransformerRegistry = getTransformers(registry, "postTransformerRegistry");

            //transformerRegistry.addAll(postTransformerRegistry);

            //for (String className : transformerRegistry)
            //    adapter.registerTransformerClass(Class.forName(className, true, loader));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void registerTransformerClass(String className) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> targetClass = Class.forName(className, true, Launch.classLoader);

        Class<?> clazz = adapter.getClass();
        Method method = clazz.getDeclaredMethod("registerTransformerClass", Class.class);
        method.setAccessible(true);
        method.invoke(adapter, targetClass);
    }

    private byte[] transform(String className, byte[] bytes) {
        try {
            Class<?> clazz = adapter.getClass();
            Method method = clazz.getDeclaredMethod("transform", String.class, byte[].class);
            method.setAccessible(true);
            return (byte[]) method.invoke(adapter, className, bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (adapter == null)
            init();
        return transform(name, bytes);
    }
}
