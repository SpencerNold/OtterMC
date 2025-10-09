package io.github.ottermc.modules.setting;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Setting;

public class StringSetting extends Setting<String> {
	
	public StringSetting(String name, String value, int lines) {
		super(name, value, Type.STRING);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeString(value);
	}

	@Override
	public void read(ByteBuf buf) {
		value = buf.readString();
	}
}
