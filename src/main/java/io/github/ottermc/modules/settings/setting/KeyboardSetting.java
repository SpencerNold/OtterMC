package io.github.ottermc.modules.settings.setting;

import org.lwjgl.input.Keyboard;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.settings.Setting;

public class KeyboardSetting extends Setting<Integer> {

	public KeyboardSetting(String name, int value) {
		super(name, value, Type.KEYBOARD);
	}
	
	public String getKeyName() {
		return Keyboard.getKeyName(value);
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
