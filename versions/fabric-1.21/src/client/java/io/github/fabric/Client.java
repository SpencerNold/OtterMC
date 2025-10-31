package io.github.fabric;

import io.github.ottermc.Game;
import io.github.ottermc.Pair;
import io.github.ottermc.Patch;
import io.github.ottermc.logging.Logger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Client implements ClientModInitializer {

    private static final Map<Class<?>, Object> OVERRIDES = new HashMap<>();

    @Override
    public void onInitializeClient() {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("minecraft");
        if (container.isPresent()) {
            String version = container.get().getMetadata().getVersion().getFriendlyString();
            try {
                Class<?> clazz = Class.forName("ottermc.Patch" + version.replace(".", ""));
                Object object = newInstanceDefault(clazz);
                if (object instanceof Patch patch) {
                    List<Pair<Class<?>, Object>> overrides = patch.getOverrides();
                    for (Pair<Class<?>, Object> override : overrides)
                        OVERRIDES.put(override.getKey(), override.getValue());
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        Game.game = new Game(new SubClient());
        Game.game.start();
    }

    public static void register(Class<?> clazz, Object object) {
        if (OVERRIDES.containsKey(clazz))
            object = OVERRIDES.get(clazz);
        try {
            Method method = clazz.getDeclaredMethod("register", clazz);
            method.setAccessible(true);
            method.invoke(null, object);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Logger.error(e);
        }
    }

    private static <T> T newInstanceDefault(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            return null;
        }
    }
}
