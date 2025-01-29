package io.github.ottermc.modules.setting;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Setting;
import io.github.ottermc.render.Color;

public class ColorSetting extends Setting<Color> {

	private boolean alpha;
	
	public ColorSetting(String name, Color value, boolean alpha) {
		super(name, value, Type.COLOR);
		this.alpha = alpha;
	}
	
	public boolean hasAlpha() {
		return alpha;
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(value.getValue());
	}

	@Override
	public void read(ByteBuf buf) {
		value = new Color(buf.readInt(), alpha);
	}
}
