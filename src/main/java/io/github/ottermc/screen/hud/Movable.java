package io.github.ottermc.screen.hud;

import io.github.ottermc.io.ByteBuf;
import net.minecraft.util.MathHelper;

public interface Movable {
	public void setX(int x);
	public void setY(int y);
	
	public int getSerialId();
	
	public default void setScale(float scale) {
		((Component) this).scale = MathHelper.clamp_float(scale, 0.0f, 3.0f);
	}
	
	public default void clamp(int displayWidth, int displayHeight) {
		if (!(this instanceof Component))
			return; // This should not happen, but if it does AAAAAAAAAAAHHHHHHHHHHHHHHH (of course)
		Component component = (Component) this;
		int x = MathHelper.clamp_int(component.getX(), 0, (int) (displayWidth - (component.getRawWidth() * component.getScale())));
		setX(x);
		int y = MathHelper.clamp_int(component.getY(), 0, (int) (displayHeight - (component.getRawHeight() * component.getScale())));
		setY(y);
	}
	
	public default void write(ByteBuf buf) {
		Component component = (Component) this;
		buf.writeInt(component.getX());
		buf.writeInt(component.getY());
		buf.writeFloat(component.getScale());
	}
	
	public default void read(ByteBuf buf) {
		setX(buf.readInt());
		setY(buf.readInt());
		setScale(buf.readFloat());
	}
}
