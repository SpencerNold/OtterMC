package io.ottermc.transformer.structures;

public class MutRef<T> {

	private T t;
	
	public MutRef(T t) {
		this.t = t;
	}
	
	public MutRef() {
		this.t = null;
	}
	
	public T get() {
		return t;
	}
	
	public void set(T t) {
		this.t = t;
	}
}
