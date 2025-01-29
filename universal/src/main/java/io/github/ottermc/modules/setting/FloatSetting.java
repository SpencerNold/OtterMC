package io.github.ottermc.modules.setting;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.NumericSetting;

public class FloatSetting extends NumericSetting<Float> {

	public FloatSetting(String name, Float value, Float min, Float max) {
		super(name, Type.FLOAT, value, min, max);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeFloat(value);
	}

	@Override
	public void read(ByteBuf buf) {
		value = buf.readFloat();
	}
}
