package io.github.ottermc.modules.settings.storable;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.settings.Storable;

public class FloatStorage extends Storable<Float> {

	public FloatStorage(float value) {
		super(value);
	}
	
	@Override
	public int getSerialId() {
		return "FLOATING_POINT_STORAGE".hashCode();
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
