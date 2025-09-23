package io.ottermc.fml189;

import io.ottermc.transformer.adapters.MinecraftClassNameAdapter;
import io.ottermc.transformer.adapters.MinecraftMethodNameAdapter;
import me.spencernold.transformer.ClassAdapter;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;
import java.util.List;

public class OMCTransformer implements IClassTransformer {

    private ClassAdapter adapter;

    private void init() {
        adapter = new ClassAdapter(Opcodes.V1_8, Opcodes.ASM5, MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class);
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> agent = Class.forName("agent.Agent", false, loader);
            Object registry = agent.getMethod("getTransformerRegistry").invoke(null);

            List<String> transformerRegistry = getTransformers(registry, "transformerRegistry");
            List<String> postTransformerRegistry = getTransformers(registry, "postTransformerRegistry");

            transformerRegistry.addAll(postTransformerRegistry);

            for (String className : transformerRegistry)
                adapter.registerTransformerClass(Class.forName(className, true, loader));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> getTransformers(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (List<String>) field.get(object);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (adapter == null)
            init();
        return adapter.transform(name, bytes);
    }
}
