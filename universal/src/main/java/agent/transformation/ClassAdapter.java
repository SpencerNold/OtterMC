package agent.transformation;

import agent.Mapping;
import agent.objects.TransformableMethod;
import agent.objects.TransformableObject;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class ClassAdapter implements ClassFileTransformer {

    private static final int ASM_MAX_MAJOR_VERSION = 63;
    private static ClassAdapter instance;

    private final Instrumentation instrumentation;

    private final Map<Class<?>, TransformableObject> transformers = new HashMap<>();

    public ClassAdapter(Instrumentation instrumentation) {
        instance = this;
        this.instrumentation = instrumentation;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> clazz, ProtectionDomain domain, byte[] buffer) throws IllegalClassFormatException {
        TransformableObject object = transformers.get(clazz);
        if (object == null)
            return buffer;
        int version = getMajorVersion(buffer);
        clampMajorVersion(buffer, 0, ASM_MAX_MAJOR_VERSION);
        ClassReader reader = new ClassReader(buffer);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        reader.accept(new ClassAdapterVisitor(object, Opcodes.ASM9, writer), 0);
        buffer = writer.toByteArray();
        setMajorVersion(buffer, version);
        return buffer;
    }

    public void register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Transformer.class))
            throw new RuntimeException(clazz.getName() + " is missing the @Transformer annotation");
        Transformer transformer = clazz.getAnnotation(Transformer.class);
        if (!Mapping.contains(transformer.name()))
            throw new RuntimeException(transformer.name() + " is not a Minecraft class");
        Mapping.Class mclass = Mapping.get(transformer.name());
        Class<?> mcClass;
        try {
            mcClass = Class.forName(mclass.getName1());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (!transformers.containsKey(mcClass))
            transformers.put(mcClass, new TransformableObject());
        TransformableObject object = transformers.get(mcClass);
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Injector.class))
                continue;
            Injector injector = method.getAnnotation(Injector.class);
            Mapping.Method mmethod = mclass.getMethod(injector.name());
            if (mmethod != null)
                object.add(mmethod.getName1() + mmethod.getDesc1(), new TransformableMethod(clazz, mmethod.getName1(), mmethod.getDesc1(), method, injector.target()));
        }
    }

    public void execute() throws UnmodifiableClassException {
        if (transformers.isEmpty())
            return;
        instrumentation.addTransformer(this, true);
        instrumentation.retransformClasses(transformers.keySet().toArray(new Class<?>[0]));
        instrumentation.removeTransformer(this);
    }

    public void clear() {
        transformers.clear();
    }

    public static String getRawClassName(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    private static void clampMajorVersion(byte[] buffer, int min, int max) {
        // Jar uses major version 65, and loads ASM 9.3 which only supports up to 63
        // this function does some evil magic to change the class file's version and change it back
        // so that asm can read the class, but hopefully have the class still function as
        // originally intended.
        // !!! May run into issues from new Java 20+ features changing bytecode !!!
        int version = getMajorVersion(buffer);
        if (version > max)
            version = max;
        else if (version < min)
            version = min;
        setMajorVersion(buffer, version);
    }

    private static int getMajorVersion(byte[] buffer) {
        return (((buffer[6] & 0xFF) << 8) | (buffer[7] & 0xFF));
    }

    private static void setMajorVersion(byte[] buffer, int version) {
        buffer[6] = (byte) ((version >> 8) & 0xFF);
        buffer[7] = (byte) (version & 0xFF);
    }

    public static ClassAdapter getInstance() {
        return instance;
    }
}
