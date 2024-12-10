package agent.objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class TClassObj {

	private final Map<String, LinkedList<TMethodObj>> injectors = new HashMap<>();
	
	private final Class<?> transformer;
	
	public TClassObj(Class<?> transformer) {
		this.transformer = transformer;
	}
	
	public void addInjector(String name, TMethodObj injector) {
		if (!injectors.containsKey(name))
			injectors.put(name, new LinkedList<TMethodObj>());
		LinkedList<TMethodObj> methods = injectors.get(name);
		methods.add(injector);
	}
	
	public LinkedList<TMethodObj> getInjector(String name) {
		if (!injectors.containsKey(name))
			return null;
		return injectors.get(name);
	}

	public Set<String> getAllNames() {
		return injectors.keySet();
	}

	public String getTransformerClassNameRaw() {
		return getRawClassName(transformer);
	}
	
	public static String getRawClassName(Class<?> clazz) {
		return clazz.getName().replace('.', '/');
	}
}
