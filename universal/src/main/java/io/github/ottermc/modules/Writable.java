package io.github.ottermc.modules;

public interface Writable<T> {
	
	void write(T buf);
	void read(T buf);
	
}
