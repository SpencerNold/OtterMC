package io.github.ottermc.modules.settings;

public interface Writable<T> {
	
	public void write(T buf);
	public void read(T buf);
	
}
