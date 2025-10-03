package io.ottermc.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ReflectObject {

    protected final Object instance;

    protected ReflectObject(Object instance) {
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    protected Object invokeMethod(String methodName, Object... parameters) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?>[] arguments = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++)
            arguments[i] = parameters[i].getClass();
        return invokeMethod(instance.getClass(), instance, methodName, arguments, parameters);
    }

    protected Object invokeMethod(Class<?> clazz, Object instance, String methodName, Class<?>[] arguments, Object[] parameters) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, arguments);
        method.setAccessible(true);
        return method.invoke(instance, parameters);
    }

    protected Object getField(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return getField(instance.getClass(), instance, fieldName);
    }

    protected Object getField(Class<?> clazz, Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    protected static Object newInstance(ClassLoader loader, String className, Object... parameters) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className, true, loader);
        Class<?>[] arguments = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++)
            arguments[i] = parameters[i].getClass();
        return newInstance(clazz, arguments, parameters);
    }

    protected static Object newInstance(Class<?> clazz, Class<?>[] arguments, Object[] parameters) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getDeclaredConstructor(arguments);
        return constructor.newInstance(parameters);
    }
}
