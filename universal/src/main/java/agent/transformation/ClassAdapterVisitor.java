package agent.transformation;

import agent.objects.TransformableMethod;
import agent.objects.TransformableObject;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import structures.Pair;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassAdapterVisitor extends ClassVisitor {

    private final TransformableObject object;

    public ClassAdapterVisitor(TransformableObject object, int api, ClassVisitor visitor) {
        super(api, visitor);
        this.object = object;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (!object.contains(name + descriptor))
            return cv.visitMethod(access, name, descriptor, signature, exceptions);
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        List<TransformableMethod> methods = object.getHeadMethods(name + descriptor);
        for (int i = 0; i < methods.size(); i++) {
            int index = getFirstLocalPos(descriptor);
            TransformableMethod method = methods.get(i);
            mv.visitTypeInsn(Opcodes.NEW, ClassAdapter.getRawClassName(Callback.class));
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, ClassAdapter.getRawClassName(Callback.class), "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ASTORE, index);
            mv.visitTypeInsn(Opcodes.NEW, method.getTransformerClassNameRaw());
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, method.getTransformerClassNameRaw(), "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ALOAD, 0);

            for (Pair<Integer, Integer> insn : method.getLoadParameterInstructions())
                mv.visitVarInsn(insn.getKey(), insn.getValue());
            mv.visitVarInsn(Opcodes.ALOAD, index);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, method.getTransformerClassNameRaw(), method.getTransformerMethodName(), method.getTransformerMethodDescriptor(), false);
            mv.visitVarInsn(Opcodes.ALOAD, index);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassAdapter.getRawClassName(Callback.class), "isNotCanceled", "()Z", false);
            Label label = new Label();
            mv.visitJumpInsn(Opcodes.IFNE, label);
            mv.visitInsn(method.getReturnType());
            mv.visitLabel(label);
        }
        return new MethodAdapterVisitor(object, name, descriptor, Opcodes.ASM9, mv);
    }

    private int getFirstLocalPos(String desc) {
        Pattern pattern = Pattern.compile("(Z|B|S|I|J|F|D|L[^(L|;)]+?;)"); // (B|S|I|J|F|D|L[^(L|;)]+?;)
        Matcher matcher = pattern.matcher(desc);
        int size = 1;
        while (matcher.find()) {
            String s = matcher.group(1);
            if (s.equals("D") || s.equals("J"))
                size += 2;
            else
                size++;
        }
        return size;
    }
}
