package io.ottermc.fml189;

import io.ottermc.common.ClassAdapter;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class OMCTransformer implements IClassTransformer {

    private static ClassAdapter adapter;
    private static OMCTransformer instance;

    public OMCTransformer() {
        instance = this;
    }

    private byte[] transform(ClassLoader classLoader, String className, byte[] bytes) {
        try {
            ensureReady();
            return adapter.transform(classLoader, className, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        return transform(ClassLoader.getSystemClassLoader(), name, bytes);
    }

    public static void register(Class<?> clazz) throws Exception {
        ensureReady();
        adapter.registerTransformerClass(clazz);
    }

    private static void ensureReady() {
        if (adapter == null)
            init();
    }

    private static void init() {
        try {
            final int v1_8 = 52;
            final int asm5 = 327680;
            Class<?> classNameAdapterClass = Class.forName("io.ottermc.transformer.adapters.MinecraftClassNameAdapter", true, Launch.classLoader);
            Class<?> methodNameAdapterClass = Class.forName("io.ottermc.transformer.adapters.MinecraftMethodNameAdapter", true, Launch.classLoader);
            adapter = new ClassAdapter(Launch.classLoader, v1_8, asm5, classNameAdapterClass, methodNameAdapterClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OMCTransformer getInstance() {
        return instance;
    }
}
