package agent.objects;

import agent.transformation.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransformableObject {

    private final Map<String, List<TransformableMethod>> methods = new HashMap<>();

    public void add(String name, TransformableMethod method) {
        if (!methods.containsKey(name))
            methods.put(name, new ArrayList<>());
        methods.get(name).add(method);
    }

    public List<TransformableMethod> getHeadMethods(String name) {
        return getMethodsFromTarget(name, Target.HEAD);
    }

    public List<TransformableMethod> getTailMethods(String name) {
        return getMethodsFromTarget(name, Target.TAIL);
    }

    private List<TransformableMethod> getMethodsFromTarget(String name, Target target) {
        List<TransformableMethod> methods = this.methods.get(name);
        if (methods == null)
            return new ArrayList<>();
        return methods.stream().filter(m -> m.getTarget() == target).collect(Collectors.toList());
    }

    public boolean contains(String name) {
        return methods.containsKey(name);
    }
}
