package io.ottermc.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StateRegistry {

    // Wee bit janky

    public static void setState(ClassLoader loader, Object object) throws Exception {
        Class<?> stateClass = Class.forName("io.ottermc.transformer.State", true, loader);
        Class<?> stateRegistryClass = Class.forName("agent.StateRegistry", true, loader);
        Method method = stateRegistryClass.getDeclaredMethod("setState", stateClass);
        method.setAccessible(true);
        method.invoke(null, object);
    }

    public static Object getInitState(ClassLoader loader) throws Exception {
        Class<?> clazz = Class.forName("io.ottermc.transformer.State", true, loader);
        Field field = clazz.getDeclaredField("INIT");
        field.setAccessible(true);
        return field.get(null);
    }

    public static Object getPostInitState(ClassLoader loader) throws Exception {
        Class<?> clazz = Class.forName("io.ottermc.transformer.State", true, loader);
        Field field = clazz.getDeclaredField("POST_INIT");
        field.setAccessible(true);
        return field.get(null);
    }
}
