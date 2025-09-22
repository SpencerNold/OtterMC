package agent;

import agent.adapters.MinecraftClassNameAdapter;
import agent.adapters.MinecraftMethodNameAdapter;
import me.spencernold.transformer.ClassAdapter;
import me.spencernold.transformer.ClassTransformException;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.List;

public class ClassTransformer implements ClassFileTransformer {

    private static ClassTransformer instance;

    private final Instrumentation instrumentation;
    private final ClassAdapter adapter;

    public ClassTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.adapter = new ClassAdapter(Opcodes.V19, Opcodes.ASM9, MinecraftClassNameAdapter.class, MinecraftMethodNameAdapter.class);
        instance = this;
    }

    public void register(Class<?> clazz) {
        try {
            adapter.registerTransformerClass(clazz);
        } catch (ClassTransformException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute() throws UnmodifiableClassException, ClassNotFoundException {
        List<String> classNames = adapter.getClassesToTransform();
        if (classNames.isEmpty())
            return;
        Class<?>[] classes = new Class[classNames.size()];
        for (int i = 0; i < classNames.size(); i++)
            classes[i] = Class.forName(classNames.get(i));
        instrumentation.addTransformer(this, true);
        instrumentation.retransformClasses(classes);
        instrumentation.removeTransformer(this);
    }

    public void clear() {
        adapter.clear();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return adapter.transform(className, classfileBuffer);
    }

    public static ClassTransformer getInstance() {
        return instance;
    }
}
