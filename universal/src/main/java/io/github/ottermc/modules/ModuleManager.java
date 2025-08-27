package io.github.ottermc.modules;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ModuleManager {

	private final LinkedList<Module> modules = new LinkedList<>();
	
	public void register(Module module) {
		modules.add(module);
	}
	
	public void forEach(Consumer<Module> consumer) {
		modules.forEach(consumer);
	}
	
	public Stream<Module> filter(Predicate<Module> predicate) {
		return modules.stream().filter(predicate);
	}

	public Module find(Predicate<Module> predicate) {
		return filter(predicate).findAny().orElse(null);
	}

	public Stream<Module> getByCategory(Category category) {
		return filter(m -> m.category == category);
	}

	public LinkedList<Module> getModules() {
		return modules;
	}

	public void iterate(Consumer<Module> consumer) {
		modules.forEach(consumer);
	}
}
