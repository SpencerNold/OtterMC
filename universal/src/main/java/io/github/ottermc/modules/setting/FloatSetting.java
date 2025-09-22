package io.github.ottermc.modules.setting;

import io.ottermc.transformer.io.ByteBuf;
import io.github.ottermc.modules.NumericSetting;

public class FloatSetting extends NumericSetting<Double> {

	public FloatSetting(String name, Double value, Double min, Double max) {
		super(name, Type.FLOAT, value, min, max);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeDouble(value);
	}

	@Override
	public void read(ByteBuf buf) {
		value = buf.readDouble();
	}
}
