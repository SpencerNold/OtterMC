package io.github.ottermc.modules.settings.setting;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.settings.Setting;

public class BooleanSetting extends Setting<Boolean> {

	public BooleanSetting(String name, boolean value) {
		super(name, value, Type.BOOLEAN);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeBoolean(value);
	}

	@Override
	public void read(ByteBuf buf) {
		value = buf.readBoolean();
	}
}
