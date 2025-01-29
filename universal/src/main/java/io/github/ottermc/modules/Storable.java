package io.github.ottermc.modules;

import io.github.ottermc.io.ByteBuf;

public abstract class Storable<T> implements Writable<ByteBuf> {

	protected T value;
	
	public Storable(T value) {
		this.value = value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public abstract int getSerialId();
}
