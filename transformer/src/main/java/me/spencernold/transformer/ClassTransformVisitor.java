package me.spencernold.transformer;

import org.objectweb.asm.ClassVisitor;

public class ClassTransformVisitor extends ClassVisitor {

    public ClassTransformVisitor(int api, ClassVisitor visitor) {
        super(api, visitor);
    }
}
