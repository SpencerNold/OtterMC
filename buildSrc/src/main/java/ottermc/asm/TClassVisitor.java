package ottermc.asm;

import org.objectweb.asm.*;
import ottermc.Mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TClassVisitor extends ClassVisitor {

	private final List<String> transformOverrideMethods = new ArrayList<>();
	private final int version;
	
	public TClassVisitor(ClassVisitor visitor, int version) {
		super(Opcodes.ASM9, visitor);
		this.version = version;
	}
	
	@Override
	public void visit(int v, int access, String name, String signature, String superName, String[] interfaces) {
		transformOverrideMethods.addAll(Hierarchy.getParentsOfClass(name));
		if (Mapping.contains(superName, version))
			superName = Mapping.get(superName, version).getName1();
		for (int i = 0; i < interfaces.length; i++) {
			String iname = interfaces[i];
			if (Mapping.contains(iname, version))
				interfaces[i] = Mapping.get(iname, version).getName1();
		}
		super.visit(v, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		for (String tom : transformOverrideMethods) {
			if (Mapping.contains(tom, version)) {
				Mapping.Class mclass = Mapping.get(tom, version);
				if (mclass.containsMethod(name, descriptor)) {
					Mapping.Method mmethod = mclass.getMethod(name, descriptor);
					name = mmethod.getName1();
					descriptor = mmethod.getDesc1();
				}
			}
		}
		if (exceptions != null) {
			for (int i = 0; i < exceptions.length; i++) {
				if (Mapping.contains(exceptions[i], version))
					exceptions[i] = Mapping.get(exceptions[i], version).getName1();
			}
		}
		descriptor = RemapVisitor.sanitizeDescriptor(descriptor, version);
		return new RemapVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), transformOverrideMethods, version);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		descriptor = RemapVisitor.sanitizeDescriptor(descriptor, version);
		return super.visitField(access, name, descriptor, signature, value);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		descriptor = RemapVisitor.sanitizeDescriptor(descriptor, version);
		return super.visitAnnotation(descriptor, visible);
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		super.visitInnerClass(name, outerName, innerName, access);
	}
}
