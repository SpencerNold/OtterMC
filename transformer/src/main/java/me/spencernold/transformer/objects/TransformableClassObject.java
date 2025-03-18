package me.spencernold.transformer.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransformableClassObject {

    private final Map<String, List<TransformableMethodObject>> methods = new HashMap<>();

    public void add(String name, TransformableMethodObject method) {
        if (!methods.containsKey(name))
            methods.put(name, new ArrayList<>());
        methods.get(name).add(method);
    }

    public List<TransformableMethodObject> get(String name) {
        return methods.getOrDefault(name, new ArrayList<>());
    }

    public List<TransformableMethodObject> getByPredicate(String name, Predicate<TransformableMethodObject> predicate) {
        return get(name).stream().filter(predicate).collect(Collectors.toList());
    }

    public boolean contains(String name) {
        return methods.containsKey(name);
    }
}
