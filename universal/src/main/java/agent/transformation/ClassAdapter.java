package agent.transformation;

import agent.Mapping;
import agent.objects.TClassObj;
import agent.objects.TMethodObj;
import org.objectweb.asm.*;
import structures.Pair;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClassAdapter implements ClassFileTransformer {

	private static final int ASM_MAX_MAJOR_VERSION = 63;

	private static ClassAdapter instance;
	
	private final Instrumentation instrumentation;

	private final Map<Class<?>, TClassObj> transformers = new HashMap<>();
	
	public ClassAdapter(Instrumentation instrumentation) {
		instance = this;
		this.instrumentation = instrumentation;
	}
	
	public void register(Class<?> clazz) {
		if (!clazz.isAnnotationPresent(Transformer.class))
			throw new RuntimeException(clazz.getName() + " is missing the @Transformer annotation");
		Transformer transformer = clazz.getAnnotation(Transformer.class);
		if (!Mapping.contains(transformer.name()))
			throw new RuntimeException(transformer.name() + " is not a Minecraft class");
		Mapping.Class mclass = Mapping.get(transformer.name());
		Class<?> mcClass;
		try {
			mcClass = Class.forName(mclass.getName1());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (!transformers.containsKey(mcClass))
			transformers.put(mcClass, new TClassObj(clazz));
		TClassObj tclass = transformers.get(mcClass);
		for (Method method : clazz.getDeclaredMethods()) {
			if (!method.isAnnotationPresent(Injector.class))
				continue;
			Injector injector = method.getAnnotation(Injector.class);
			Mapping.Method mmethod = mclass.getMethod(injector.name());
			TMethodObj tmethod;
			if (mmethod != null)
				tmethod = new TMethodObj(mmethod.getName1(), mmethod.getDesc1(), method, injector.target());
			else
				tmethod = TMethodObj.getFromJoined(injector.name(), method, injector.target());
			tclass.addInjector(tmethod.getJoined(), tmethod);
		}
	}
	
	public void execute() throws UnmodifiableClassException {
		if (transformers.isEmpty())
			return;
		instrumentation.addTransformer(this, true);
		instrumentation.retransformClasses(transformers.keySet().toArray(new Class<?>[0]));
		instrumentation.removeTransformer(this);
	}
	
	public void clear() {
		transformers.clear();
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> clazz, ProtectionDomain domain, byte[] buffer) {
		TClassObj tclass = transformers.get(clazz);
		if (tclass == null)
			return buffer;
		int version = getMajorVersion(buffer);
		clampMajorVersion(buffer, 0, ASM_MAX_MAJOR_VERSION);
		ClassReader reader = new ClassReader(buffer);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
				LinkedList<TMethodObj> tmethods = tclass.getInjector(name + descriptor);
				if (tmethods == null)
					return super.visitMethod(access, name, descriptor, signature, exceptions);
				MethodVisitor mv = new MethodVisitor(Opcodes.ASM9, cv.visitMethod(access, name, descriptor, signature, exceptions)) {
					public void visitInsn(int opcode) {
						if (opcode == Opcodes.RETURN || opcode == Opcodes.ARETURN || opcode == Opcodes.IRETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN) {
							for (TMethodObj tmethod : tmethods) {
								if (tmethod.getTarget() == Target.TAIL)
									injectInto(tmethod, this, true);
							}
						}
						super.visitInsn(opcode);
					}
				};
				for (TMethodObj tmethod : tmethods) {
					if (tmethod.getTarget() == Target.HEAD)
						injectInto(tmethod, mv, false);
				}
				return mv;
			}
			
			private void injectInto(TMethodObj tmethod, MethodVisitor mv, boolean tail) {
				// Callback
				mv.visitTypeInsn(Opcodes.NEW, TClassObj.getRawClassName(Callback.class));
				mv.visitInsn(Opcodes.DUP);
				mv.visitMethodInsn(Opcodes.INVOKESPECIAL, TClassObj.getRawClassName(Callback.class), "<init>", "()V", false);
				int cbPos = tmethod.getLocalSize();
				mv.visitVarInsn(Opcodes.ASTORE, cbPos);
				// TClass
				mv.visitTypeInsn(Opcodes.NEW, tclass.getTransformerClassNameRaw());
				mv.visitInsn(Opcodes.DUP);
				mv.visitMethodInsn(Opcodes.INVOKESPECIAL, tclass.getTransformerClassNameRaw(), "<init>", "()V", false);
				// Calling the injector
				mv.visitVarInsn(Opcodes.ALOAD, 0);
				for (Pair<Integer, Integer> insn : tmethod.getLoadParameterInstructions())
					mv.visitVarInsn(insn.getKey(), insn.getValue());
				mv.visitVarInsn(Opcodes.ALOAD, cbPos);
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, tclass.getTransformerClassNameRaw(), tmethod.getTransformerName(), tmethod.getTransformerDescriptor(), false);
				if (!tail) {
					// Canceling the event if needed
					// I'm not sure how to easily append instructions to the end of a method,
					// so I'm using this messy way instead
					mv.visitVarInsn(Opcodes.ALOAD, cbPos);
					mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, TClassObj.getRawClassName(Callback.class), "isNotCanceled", "()Z", false);
					Label label = new Label();
					mv.visitJumpInsn(Opcodes.IFNE, label);
					mv.visitInsn(tmethod.getReturnType());
					mv.visitLabel(label);
				}
			}
			
		}, 0);
		buffer = writer.toByteArray();
		setMajorVersion(buffer, version);
        return buffer;
	}

	private static void clampMajorVersion(byte[] buffer, int min, int max) {
		// Jar uses major version 65, and loads ASM 9.3 which only supports up to 63
		// this function does some evil magic to change the class file's version and change it back
		// so that asm can read the class, but hopefully have the class still function as
		// originally intended.
		// !!! May run into issues from new Java 20+ features changing bytecode !!!
		int version = getMajorVersion(buffer);
		if (version > max)
			version = max;
		else if (version < min)
			version = min;
		setMajorVersion(buffer, version);
	}

	private static int getMajorVersion(byte[] buffer) {
		return (((buffer[6] & 0xFF) << 8) | (buffer[7] & 0xFF));
	}

	private static void setMajorVersion(byte[] buffer, int version) {
		buffer[6] = (byte) ((version >> 8) & 0xFF);
		buffer[7] = (byte) (version & 0xFF);
	}

	public static ClassAdapter getInstance() {
		return instance; // For POST-Initialization
	}
}
