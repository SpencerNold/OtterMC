package io.github.ottermc.modules.settings.setting;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.settings.Setting;

public class StringSetting extends Setting<String> {
	
	private int lines;

	public StringSetting(String name, String value, int lines) {
		super(name, value, Type.STRING);
		this.lines = lines;
	}
	
	public int getLines() {
		return lines;
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
