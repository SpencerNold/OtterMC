package agent.objects;

import agent.transformation.ClassAdapter;
import agent.transformation.Target;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import structures.Pair;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class TransformableMethod {

    private final Class<?> clazz;
    private final String name, desc;
    private final Method method;
    private final Target target;

    public TransformableMethod(Class<?> clazz, String name, String desc, Method method, Target target) {
        this.clazz = clazz;
        this.name = name;
        this.desc = desc;
        this.method = method;
        this.target = target;
    }

    public String getTransformerClassNameRaw() {
        return ClassAdapter.getRawClassName(clazz);
    }

    public List<Pair<Integer, Integer>> getLoadParameterInstructions() {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        int index = 1;
        int count = method.getParameterCount() - 1;
        for (int i = 1; i < count; i++) {
            Parameter param = method.getParameters()[i];
            Class<?> type = param.getType();
            int insn = Opcodes.ALOAD;
            if (type == byte.class || type == short.class || type == int.class || type == boolean.class)
                insn = Opcodes.ILOAD;
            if (type == long.class)
                insn = Opcodes.LLOAD;
            if (type == float.class)
                insn = Opcodes.FLOAD;
            if (type == double.class)
                insn = Opcodes.DLOAD;
            list.add(new Pair<>(insn, index));
            index += (type == double.class || type == long.class) ? 2 : 1;
        }
        return list;
    }

    public int getReturnType() {
        if (desc.endsWith("V"))
            return Opcodes.RETURN;
        else if (desc.endsWith(";"))
            return Opcodes.ARETURN;
        else if (desc.endsWith("B") || desc.endsWith("S") || desc.endsWith("I") || desc.endsWith("Z"))
            return Opcodes.IRETURN;
        else if (desc.endsWith("J"))
            return Opcodes.LRETURN;
        else if (desc.endsWith("F"))
            return Opcodes.FRETURN;
        else if (desc.endsWith("D"))
            return Opcodes.DRETURN;
        return -1;
    }

    public Class<?> getParentClass() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return desc;
    }

    public String getTransformerMethodName() {
        return method.getName();
    }

    public String getTransformerMethodDescriptor() {
        return Type.getMethodDescriptor(method);
    }

    public Method getMethod() {
        return method;
    }

    public Target getTarget() {
        return target;
    }
}
