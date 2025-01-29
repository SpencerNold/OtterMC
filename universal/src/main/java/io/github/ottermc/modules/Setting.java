package io.github.ottermc.modules;

public abstract class Setting<T> extends Storable<T> {

	protected final String name;
	private final Type type;
	
	public Setting(String name, T value, Type type) {
		super(value);
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public int getSerialId() {
		return name.hashCode();
	}
	
	public enum Type {
		BOOLEAN, COLOR, ENUM, INT, FLOAT, KEYBOARD, STRING;
	}
}
