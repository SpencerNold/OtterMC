package io.ottermc.transformer;

import io.ottermc.transformer.adapters.MinecraftClassNameAdapter;
import io.ottermc.transformer.adapters.MinecraftMethodNameAdapter;
import me.spencernold.transformer.ClassAdapter;

public interface Transformable {

    TransformerInitializationHook TRANSFORMABLE_HOOK = null;

    void register(Class<?> clazz);

    byte[] transform(String className, byte[] bytes);

    static ClassAdapter createClassAdapter(int maxJava, int maxASM) {
        return new ClassAdapter(maxJava, maxASM, MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class);
    }
}
