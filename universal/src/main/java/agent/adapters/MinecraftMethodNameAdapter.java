package agent.adapters;

import agent.Mapping;
import me.spencernold.transformer.adapters.MethodNameAdapter;

public class MinecraftMethodNameAdapter extends MethodNameAdapter {

    private final Mapping.Class mappingClass;

    public MinecraftMethodNameAdapter(String classNameRaw) {
        super(classNameRaw);
        this.mappingClass = Mapping.get(classNameRaw);
    }

    @Override
    public String adapt(String methodName) {
        Mapping.Method method = mappingClass.getMethod(methodName);
        return method.getName1() + method.getDesc1();
    }
}
