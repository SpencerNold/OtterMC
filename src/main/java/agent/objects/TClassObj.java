package agent.objects;

import java.util.HashMap;
import java.util.Map;

public class TClassObj {

	private final Map<String, TMethodObj> injectors = new HashMap<>();
	
	private final Class<?> transformer;
	
	public TClassObj(Class<?> transformer) {
		this.transformer = transformer;
	}
	
	public void addInjector(String name, TMethodObj injector) {
		injectors.put(name, injector);
	}
	
	public TMethodObj getInjector(String name) {
		return injectors.get(name);
	}
	
	public String getTransformerClassNameRaw() {
		return getRawClassName(transformer);
	}
	
	public static String getRawClassName(Class<?> clazz) {
		return clazz.getName().replace('.', '/');
	}
}
