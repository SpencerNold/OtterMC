package io.github.ottermc.modules.storable;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Storable;

public class IntStorage extends Storable<Integer> {

	public IntStorage(Integer value) {
		super(value);
	}
	
	@Override
	public int getSerialId() {
		return "INTEGER_STORAGE".hashCode();
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
