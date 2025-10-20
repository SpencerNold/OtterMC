package ottermc.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ottermc.Mapping;

public class RemapVisitor extends MethodVisitor {

    private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile("L(.*?);");

    private final List<String> transformOverrideMethods;
    private final int version;

    public RemapVisitor(MethodVisitor visitor, List<String> transformOverrideMethods, int version) {
        super(Opcodes.ASM9, visitor);
        this.transformOverrideMethods = transformOverrideMethods;
        this.version = version;
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (Mapping.contains(owner, version)) {
            Mapping.Class clazz = Mapping.get(owner, version);
            owner = clazz.getName1();
            if (clazz.containsField(name)) {
                Mapping.Field field = clazz.getField(name);
                name = field.getName1();
            }
        }
        descriptor = sanitizeDescriptor(descriptor, version);
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (Mapping.contains(owner, version)) {
            Mapping.Class clazz = Mapping.get(owner, version);
            owner = clazz.getName1();
            if (clazz.containsMethod(name, descriptor)) {
                Mapping.Method method = clazz.getMethod(name, descriptor);
                name = method.getName1();
                descriptor = method.getDesc1();
            }
        }
        if (!Hierarchy.isClientClass(owner)) {
            for (String tom : transformOverrideMethods) {
                if (Mapping.contains(tom, version)) {
                    Mapping.Class clazz = Mapping.get(tom, version);
                    if (clazz.containsMethod(name, descriptor)) {
                        Mapping.Method method = clazz.getMethod(name, descriptor);
                        // TODO This needs work in the future
                        owner = clazz.getName1();
                        name = method.getName1();
                        descriptor = method.getDesc1();
                        break;
                    }
                }
            }
        }
        descriptor = sanitizeDescriptor(descriptor, version);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (value instanceof Type) {
            if (((Type) value).getSort() == Type.OBJECT) {
                String name = ((Type) value).getInternalName();
                if (Mapping.contains(name, version)) {
                    Mapping.Class clazz = Mapping.get(name, version);
                    value = Type.getObjectType(clazz.getName1());
                }
            }
        }
        super.visitLdcInsn(value);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (Mapping.contains(type, version)) {
            Mapping.Class clazz = Mapping.get(type, version);
            type = clazz.getName1();
        }
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        descriptor = sanitizeDescriptor(descriptor, version);
        for (int i = 0; i < bootstrapMethodArguments.length; i++) {
            Object obj = bootstrapMethodArguments[i];
            if (obj instanceof Type) {
                Type type = (Type) obj;
                String desc = sanitizeDescriptor(type.getDescriptor(), version);
                type = Type.getType(desc);
                bootstrapMethodArguments[i] = type;
            } else if (obj instanceof Handle) {
                Handle handle = (Handle) obj;
                String desc = sanitizeDescriptor(handle.getDesc(), version);
                handle = new Handle(handle.getTag(), handle.getOwner(), handle.getName(), desc, handle.isInterface());
                bootstrapMethodArguments[i] = handle;
            }
        }
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, sanitizeDescriptor(descriptor, version), signature, start, end, index);
    }

    public static String sanitizeDescriptor(String descriptor, int version) {
        Matcher matcher = DESCRIPTOR_PATTERN.matcher(descriptor);
        String descCopy = descriptor;
        while (matcher.find()) {
            String desc0 = matcher.group(1);
            if (Mapping.contains(desc0, version))
                descCopy = descCopy.replace(desc0, Mapping.get(desc0, version).getName1());
        }
        return descCopy;
    }
}