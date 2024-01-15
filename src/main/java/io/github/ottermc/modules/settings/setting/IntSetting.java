package io.github.ottermc.modules.settings.setting;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.settings.NumericSetting;

public class IntSetting extends NumericSetting<Integer> {

	public IntSetting(String name, int value, int min, int max) {
		super(name, Type.INT, value, min, max);
	}
	
	public IntSetting(String name, Type type, int value, int min, int max) {
		super(name, type, value, min, max);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(value);
	}

	@Override
	public void read(ByteBuf buf) {
		value = buf.readInt();
	}
}
