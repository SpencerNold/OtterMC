package agent.transformation;

import agent.objects.TransformableMethod;
import agent.objects.TransformableObject;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class MethodAdapterVisitor extends MethodVisitor {

    private final TransformableObject object;
    private final String name, desc;

    public MethodAdapterVisitor(TransformableObject object, String name, String desc, int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        this.name = name;
        this.desc = desc;
        this.object = object;
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN || opcode == Opcodes.ARETURN || opcode == Opcodes.IRETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN) {
            List<TransformableMethod> methods = object.getTailMethods(name + desc);

        }
    }
}
