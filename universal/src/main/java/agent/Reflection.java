package agent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Type;

public class Reflection {
	
	private static final Map<String, Method> methods = new HashMap<>();
	private static final Map<String, Field> fields = new HashMap<>();

	public static Object invokeMinecraft(String className, String methodName, Object instance, Object... arguments) {
		if (!methods.containsKey(className + methodName)) {
			if (!Mapping.contains(className))
				throw new ReflectionException(className + " is not a valid minecraft class");
			Mapping.Class mclass = Mapping.get(className);
			if (!mclass.containsMethod(methodName))
				throw new ReflectionException(methodName + " is not in type " + className);
			Mapping.Method mmethod = mclass.getMethod(methodName);
			try {
				Class<?> clazz = Class.forName(mclass.getName1().replace('/', '.'));
				Method method = clazz.getDeclaredMethod(mmethod.getName1(), getParametersFromDescriptor(mmethod.getDesc1()));
				method.setAccessible(true);
				methods.put(className + methodName, method);
			} catch (Exception e) {
				throw new ReflectionException(e);
			}
		}
		try {
			return methods.get(className + methodName).invoke(instance, arguments);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}
	
	public static void setMinecraftField(String className, String fieldName, Object instance, Object value) {
		if (!fields.containsKey(className + fieldName)) {
			if (!Mapping.contains(className))
				throw new ReflectionException(className + " is not a valid minecraft class");
			Mapping.Class mclass = Mapping.get(className);
			if (!mclass.containsField(fieldName))
				throw new ReflectionException(fieldName + " is not in type " + className);
			Mapping.Field mfield = mclass.getField(fieldName);
			try {
				Class<?> clazz = Class.forName(mclass.getName1().replace('/', '.'));
				Field field = clazz.getDeclaredField(mfield.getName1());
				field.setAccessible(true);
				fields.put(className + fieldName, field);
			} catch (Exception e) {
				throw new ReflectionException(e);
			}
		}
		try {
			fields.get(className + fieldName).set(instance, value);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}
	
	public static Object getMinecraftField(String className, String fieldName, Object instance) {
		if (!fields.containsKey(className + fieldName)) {
			if (!Mapping.contains(className))
				throw new ReflectionException(className + " is not a valid minecraft class");
			Mapping.Class mclass = Mapping.get(className);
			if (!mclass.containsField(fieldName))
				throw new ReflectionException(fieldName + " is not in type " + className);
			Mapping.Field mfield = mclass.getField(fieldName);
			try {
				Class<?> clazz = Class.forName(mclass.getName1().replace('/', '.'));
				Field field = clazz.getDeclaredField(mfield.getName1());
				field.setAccessible(true);
				fields.put(className + fieldName, field);
			} catch (Exception e) {
				throw new ReflectionException(e);
			}
		}
		try {
			return fields.get(className + fieldName).get(instance);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}
	
	private static Class<?>[] getParametersFromDescriptor(String desc) {
		Type[] types = Type.getArgumentTypes(desc);
		Class<?>[] classes = new Class[types.length];
		for (int i = 0; i < types.length; i++) {
			String name = types[i].getClassName();
			if (name.equals(boolean.class.getName()))
				classes[i] = boolean.class;
			else if (name.equals(byte.class.getName()))
				classes[i] = byte.class;
			else if (name.equals(short.class.getName()))
				classes[i] = short.class;
			else if (name.equals(int.class.getName()))
				classes[i] = int.class;
			else if (name.equals(long.class.getName()))
				classes[i] = long.class;
			else if (name.equals(float.class.getName()))
				classes[i] = float.class;
			else if (name.equals(double.class.getName()))
				classes[i] = double.class;
			else {
				try {
					classes[i] = Class.forName(name);
				} catch (ClassNotFoundException e) {
					throw new ReflectionException(e);
				}
			}
		}
		return classes;
	}
	
	private static final class ReflectionException extends RuntimeException {
		
		private static final long serialVersionUID = 3973713345576671855L;

		public ReflectionException(Throwable e) {
			super(e);
		}
		
		public ReflectionException(String e) {
			super(e);
		}
	}
}
