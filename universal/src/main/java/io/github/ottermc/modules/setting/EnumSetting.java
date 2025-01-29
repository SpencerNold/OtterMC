package io.github.ottermc.modules.setting;

public class EnumSetting<T extends Enum<T>> extends IntSetting {
	
	private final Class<T> clazz;

	public EnumSetting(String name, Class<T> clazz, T value) {
		super(name, Type.ENUM, value.ordinal(), 0, clazz.getEnumConstants().length);
		this.clazz = clazz;
	}
	
	public T getEnumValue() {
		return clazz.getEnumConstants()[value];
	}
	
	public void setEnumValue(T value) {
		this.value = value.ordinal();
	}
}
