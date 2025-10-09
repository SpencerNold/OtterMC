package io.github.ottermc.modules;

public abstract class NumericSetting<T extends Number> extends Setting<T> {

	protected final T min, max;
	
	public NumericSetting(String name, Type type, T value, T min, T max) {
		super(name, value, type);
		this.min = min;
		this.max = max;
	}
	
	public T getMinimum() {
		return min;
	}
	
	public T getMaximum() {
		return max;
	}
}
