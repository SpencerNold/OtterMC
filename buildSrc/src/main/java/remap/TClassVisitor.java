package remap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TClassVisitor extends ClassVisitor {

	private final List<String> transformOverrideMethods = new ArrayList<>();
	
	public TClassVisitor(ClassVisitor visitor) {
		super(Opcodes.ASM9, visitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		transformOverrideMethods.add(superName);
		transformOverrideMethods.addAll(Arrays.asList(interfaces));
		if (Mapping.contains(superName))
			superName = Mapping.get(superName).getName1();
		for (int i = 0; i < interfaces.length; i++) {
			String iname = interfaces[i];
			if (Mapping.contains(iname))
				interfaces[i] = Mapping.get(iname).getName1();
		}
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		for (String tom : transformOverrideMethods) {
			if (Mapping.contains(tom)) {
				Mapping.Class mclass = Mapping.get(tom);
				if (mclass.containsMethod(name, descriptor)) {
					Mapping.Method mmethod = mclass.getMethod(name, descriptor);
					name = mmethod.getName1();
					descriptor = mmethod.getDesc1();
				}
			}
		}
		if (exceptions != null) {
			for (int i = 0; i < exceptions.length; i++) {
				if (Mapping.contains(exceptions[i]))
					exceptions[i] = Mapping.get(exceptions[i]).getName1();
			}
		}
		descriptor = RemapVisitor.sanitizeDescriptor(descriptor);
		return new RemapVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		descriptor = RemapVisitor.sanitizeDescriptor(descriptor);
		return super.visitField(access, name, descriptor, signature, value);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		descriptor = RemapVisitor.sanitizeDescriptor(descriptor);
		return super.visitAnnotation(descriptor, visible);
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		super.visitInnerClass(name, outerName, innerName, access);
	}
}
