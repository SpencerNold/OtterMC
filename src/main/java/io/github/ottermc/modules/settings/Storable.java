package io.github.ottermc.modules.settings;

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
