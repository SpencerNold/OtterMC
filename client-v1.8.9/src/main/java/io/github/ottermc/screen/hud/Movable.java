package io.github.ottermc.screen.hud;

import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Writable;
import net.minecraft.util.MathHelper;

public interface Movable extends Writable<ByteBuf> {
	void setX(int x);
	void setY(int y);
	
	int getSerialId();
	
	default void setScale(float scale) {
		((Component) this).scale = MathHelper.clamp_float(scale, 0.0f, 3.0f);
	}
	
	default void clamp(int displayWidth, int displayHeight) {
		if (!(this instanceof Component))
			return; // This should not happen, but if it does AAAAAAAAAAAHHHHHHHHHHHHHHH (of course)
		Component component = (Component) this;
		int x = MathHelper.clamp_int(component.getX(), 0, (int) (displayWidth - (component.getRawWidth() * component.getScale())));
		setX(x);
		int y = MathHelper.clamp_int(component.getY(), 0, (int) (displayHeight - (component.getRawHeight() * component.getScale())));
		setY(y);
	}
	
	default void write(ByteBuf buf) {
		Component component = (Component) this;
		buf.writeInt(component.getX());
		buf.writeInt(component.getY());
		buf.writeFloat(component.getScale());
	}
	
	default void read(ByteBuf buf) {
		setX(buf.readInt());
		setY(buf.readInt());
		setScale(buf.readFloat());
	}
}
