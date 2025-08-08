package agent.adapters;

import agent.Mapping;
import me.spencernold.transformer.adapters.ClassNameAdapter;

public class MinecraftClassNameAdapter extends ClassNameAdapter {
    @Override
    public String adapt(String className) {
        Mapping.Class clazz = Mapping.get(className.replace('.', '/'));
        if (clazz == null)
            return className;
        return clazz.getName1();
    }
}
