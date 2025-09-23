package io.github.ottermc.modules.setting;

import io.github.ottermc.keybind.UniversalKeyboard;
import io.ottermc.transformer.io.ByteBuf;
import io.github.ottermc.modules.Setting;

public class KeyboardSetting extends Setting<Integer> {

	public KeyboardSetting(String name, int value) {
		super(name, value, Type.KEYBOARD);
	}
	
	public String getKeyName() {
		return value == -1 ? "" : UniversalKeyboard.translateKeyToName(value);
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
