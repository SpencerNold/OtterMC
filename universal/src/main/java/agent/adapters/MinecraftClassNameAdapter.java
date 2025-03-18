package agent.adapters;

import agent.Mapping;
import me.spencernold.transformer.adapters.ClassNameAdapter;

public class MinecraftClassNameAdapter extends ClassNameAdapter {
    @Override
    public String adapt(String className) {
        return Mapping.get(className).getName1();
    }
}
