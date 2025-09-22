package io.ottermc.forge;

import io.ottermc.addon.ExceptionHandler;
import io.ottermc.transformer.Transformable;
import me.spencernold.transformer.ClassAdapter;
import me.spencernold.transformer.ClassTransformException;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;

public class ForgeTransformer implements IClassTransformer, Transformable {

    private final ExceptionHandler handler = Throwable::printStackTrace;
    private final ClassAdapter adapter;

    public ForgeTransformer() {
        adapter = Transformable.createClassAdapter(Opcodes.V1_8, Opcodes.ASM5);
    }

    @Override
    public void register(Class<?> clazz) {
        try {
            adapter.registerTransformerClass(clazz);
        } catch (ClassTransformException e) {
            handler.handle(e);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        return transform(name, bytes);
    }

    @Override
    public byte[] transform(String className, byte[] bytes) {
        return adapter.transform(className, bytes);
    }
}
