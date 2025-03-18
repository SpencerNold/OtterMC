package me.spencernold.transformer.visitors;

import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.objects.TransformableMethodObject;
import me.spencernold.transformer.struct.Pair;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class MethodTransformVisitor extends MethodVisitor {

    private final TargetProvider[] indices = new TargetProvider[256];
    private final HashMap<TargetProvider, List<TransformableMethodObject>> methods = new HashMap<>();

    private final String name, desc;

    private int localIndex;
    private boolean isFirst;

    public MethodTransformVisitor(List<TransformableMethodObject> methodObjects, String name, String desc, int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        this.name = name;
        this.desc = desc;
        this.localIndex = VisitorTool.getFirstLocalPos(desc);
        this.isFirst = true;
        for (TransformableMethodObject method : methodObjects) {
            TargetProvider provider = new TargetProvider(method.getOpcode(), method.getOrdinal());
            if (method.getTarget() == Target.HEAD)
                provider = TargetProvider.HEAD;
            else if (method.getTarget() == Target.TAIL)
                provider = TargetProvider.TAIL;
            if (!methods.containsKey(provider))
                methods.put(provider, new ArrayList<>());
            methods.get(provider).add(method);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        visitCodeInsn(opcode);
        super.visitInsn(opcode);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        visitCodeInsn(opcode);
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitIincInsn(int varIndex, int increment) {
        visitCodeInsn(IINC);
        super.visitIincInsn(varIndex, increment);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        visitCodeInsn(opcode);
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        visitCodeInsn(opcode);
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        visitCodeInsn(LDC);
        super.visitLdcInsn(value);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        visitCodeInsn(opcode);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        visitCodeInsn(opcode);
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        if (opcode == ASTORE || opcode == ISTORE || opcode == FSTORE)
            localIndex += 1;
        else if (opcode == LSTORE || opcode == DSTORE)
            localIndex += 2;
        visitCodeInsn(opcode);
        super.visitVarInsn(opcode, varIndex);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        visitCodeInsn(INVOKEDYNAMIC);
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        visitCodeInsn(LOOKUPSWITCH);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        visitCodeInsn(TABLESWITCH);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        visitCodeInsn(MULTIANEWARRAY);
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    private void visitCodeInsn(int opcode) {
        if (isFirst) {
            List<TransformableMethodObject> methodObjects = methods.get(TargetProvider.HEAD);
            if (methodObjects != null)
                transform(methodObjects);
            isFirst = false;
        }
        if (opcode == RETURN || opcode == IRETURN  || opcode == LRETURN || opcode == FRETURN || opcode == DRETURN || opcode == ARETURN) {
            List<TransformableMethodObject> methodObjects = methods.get(TargetProvider.TAIL);
            if (methodObjects != null)
                transform(methodObjects);
        }
        TargetProvider count = indices[opcode] == null ? new TargetProvider(opcode, 0) : indices[opcode];
        List<TransformableMethodObject> methodObjects = methods.get(count);
        if (methodObjects != null)
            transform(methodObjects);
        count.increment();
        indices[opcode] = count;
    }

    private void transform(List<TransformableMethodObject> methodObjects) {
        for (TransformableMethodObject method : methodObjects) {
            // new Callback()
            String callbackClassName = VisitorTool.getRawClassName(Callback.class);
            mv.visitTypeInsn(Opcodes.NEW, callbackClassName);
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, callbackClassName, "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ASTORE, localIndex);
            // new TransformerClass()
            String transformerClassName = VisitorTool.getRawClassName(method.getTransformerClass());
            mv.visitTypeInsn(Opcodes.NEW, transformerClassName);
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, transformerClassName, "<init>", "()V", false);
            // TransformerClass::onFunction
            mv.visitVarInsn(ALOAD, 0);
            for (Pair<Integer, Integer> insn : method.getLoadParameterInstructions())
                mv.visitVarInsn(insn.getKey(), insn.getValue());
            mv.visitVarInsn(Opcodes.ALOAD, localIndex);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, transformerClassName, method.getTransformerMethodName(), method.getTransformerMethodDescriptor(), false);
            // s = Callback::isNotCanceled
            mv.visitVarInsn(Opcodes.ALOAD, localIndex);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, callbackClassName, "isNotCanceled", "()Z", false);
            // if (s) then return
            Label label = new Label();
            mv.visitJumpInsn(Opcodes.IFNE, label);
            mv.visitInsn(method.getReturnType());
            mv.visitLabel(label);
        }
    }

    private static class TargetProvider {

        private static final TargetProvider HEAD = new TargetProvider(0, 0);
        private static final TargetProvider TAIL = new TargetProvider(0, Integer.MAX_VALUE);

        private final int opcode;
        private int count;

        private TargetProvider(int opcode, int count) {
            this.opcode = opcode;
            this.count = count;
        }

        public void increment() {
            count = count + 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TargetProvider))
                return false;
            TargetProvider target = (TargetProvider) obj;
            return target.opcode == opcode && target.count == count;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new int[] { opcode, count });
        }

        @Override
        public String toString() {
            return String.format("{opcode: %s, count: %s}", opcode, count);
        }
    }
}
