package ottermc;

import io.github.ottermc.Pair;
import io.github.ottermc.Patch;
import io.github.ottermc.universal.UDrawable;
import io.github.ottermc.universal.UKeyboard;
import ottermc.v1218.ClientDrawable;
import ottermc.v1218.GLFWKeyboard;

import java.util.List;

public class Patch1218 implements Patch {
    @Override
    public List<Pair<Class<?>, Object>> getOverrides() {
        return List.of(
                new Pair<>(UKeyboard.class, new GLFWKeyboard()),
                new Pair<>(UDrawable.class, new ClientDrawable())
        );
    }
}
