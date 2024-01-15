package remap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class RemapVisitor extends MethodVisitor {

	private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile("L(.*?);");

	RemapVisitor(MethodVisitor visitor) {
		super(Opcodes.ASM9, visitor);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		if (Mapping.contains(owner)) {
			Mapping.Class clazz = Mapping.get(owner);
			owner = clazz.getName1();
			if (clazz.containsField(name)) {
				Mapping.Field field = clazz.getField(name);
				name = field.getName1();
			}
		}
		descriptor = sanitizeDescriptor(descriptor);
		super.visitFieldInsn(opcode, owner, name, descriptor);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		if (Mapping.contains(owner)) {
			Mapping.Class clazz = Mapping.get(owner);
			owner = clazz.getName1();
			if (clazz.containsMethod(name, descriptor)) {
				Mapping.Method method = clazz.getMethod(name, descriptor);
				name = method.getName1();
				descriptor = method.getDesc1();
			}
		}
		descriptor = sanitizeDescriptor(descriptor);
		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
	}

	@Override
	public void visitLdcInsn(Object value) {
		if (value instanceof Type) {
			if (((Type) value).getSort() == Type.OBJECT) {
				String name = ((Type) value).getInternalName();
				if (Mapping.contains(name)) {
					Mapping.Class clazz = Mapping.get(name);
					value = Type.getObjectType(clazz.getName1());
				}
			}
		}
		super.visitLdcInsn(value);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (Mapping.contains(type)) {
			Mapping.Class clazz = Mapping.get(type);
			type = clazz.getName1();
		}
		super.visitTypeInsn(opcode, type);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
		descriptor = sanitizeDescriptor(descriptor);
		for (int i = 0; i < bootstrapMethodArguments.length; i++) {
			Object obj = bootstrapMethodArguments[i];
			if (obj instanceof Type) {
				Type type = (Type) obj;
				String desc = sanitizeDescriptor(type.getDescriptor());
				type = Type.getType(desc);
				bootstrapMethodArguments[i] = type;
			} else if (obj instanceof Handle) {
				Handle handle = (Handle) obj;
				String desc = sanitizeDescriptor(handle.getDesc());
				handle = new Handle(handle.getTag(), handle.getOwner(), handle.getName(), desc, handle.isInterface());
				bootstrapMethodArguments[i] = handle;
			}
		}
		super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
	}
	
	@Override
	public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, sanitizeDescriptor(descriptor), signature, start, end, index);
	}

	public static String sanitizeDescriptor(String descriptor) {
		Matcher matcher = DESCRIPTOR_PATTERN.matcher(descriptor);
		String descCopy = descriptor;
		while (matcher.find()) {
			String desc0 = matcher.group(1);
			if (Mapping.contains(desc0))
				descCopy = descCopy.replace(desc0, Mapping.get(desc0).getName1());
		}
		return descCopy;
	}
}