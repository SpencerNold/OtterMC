package agent.adapters;

import agent.Mapping;
import me.spencernold.transformer.adapters.MethodNameAdapter;

public class MinecraftMethodNameAdapter extends MethodNameAdapter {

    private final Mapping.Class clazz;

    public MinecraftMethodNameAdapter(String classNameRaw) {
        super(classNameRaw);
        this.clazz = Mapping.get(classNameRaw);
    }

    @Override
    public String adapt(String methodName) {
        Mapping.Method method = clazz.getMethod(methodName);
        if (method == null)
            return methodName;
        return method.getName1() + method.getDesc1();
    }
}
