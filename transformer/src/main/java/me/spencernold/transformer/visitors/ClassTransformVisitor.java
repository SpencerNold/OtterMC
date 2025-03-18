package me.spencernold.transformer.visitors;

import me.spencernold.transformer.objects.TransformableClassObject;
import me.spencernold.transformer.objects.TransformableMethodObject;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class ClassTransformVisitor extends ClassVisitor {

    private final TransformableClassObject classObject;

    public ClassTransformVisitor(TransformableClassObject classObject, int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.classObject = classObject;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        List<TransformableMethodObject> methodObjects = classObject.get(name + descriptor);
        return new MethodTransformVisitor(methodObjects, name, descriptor, api, mv);
    }
}
