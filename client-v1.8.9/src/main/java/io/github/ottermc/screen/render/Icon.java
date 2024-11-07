package io.github.ottermc.screen.render;

import io.github.ottermc.ClientLogger;
import io.github.ottermc.render.GenericImageObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Icon extends GenericImageObject {

	public static final Icon CHECK = Icon.getIconIgnoreException("check_icon.png");
	public static final Icon MOVE = Icon.getIconIgnoreException("move_icon.png");
	public static final Icon HAT = Icon.getIconIgnoreException("hat_icon.png");
	public static final Icon GEAR = Icon.getIconIgnoreException("gear_icon.png");
	public static final Icon DROPDOWN = Icon.getIconIgnoreException("dropdown_icon.png");
	public static final Icon ADD = Icon.getIconIgnoreException("add_icon.png");
	public static final Icon CLOSE = Icon.getIconIgnoreException("close_icon.png");
	
	public static final Icon CIRCLE = Icon.getIconIgnoreException("circle_icon.png");

	public static final Icon OTTER = Icon.getIconIgnoreException("otter_icon.png");
	
	public Icon(String name) throws IOException {
		super(Icon.class, name);
	}
	
	public static Icon getIconIgnoreException(String name) {
		try {
			return new Icon("/icons/" + name);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ByteBuffer readIconToBuffer(String name) {
		InputStream input = Icon.class.getResourceAsStream("/icons/" + name);
		try {
			if (input == null)
				return null;
			BufferedImage image = ImageIO.read(input);
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			ByteBuffer buf = ByteBuffer.allocate(4 * pixels.length);
			for (int i : pixels)
				buf.putInt(i << 8 | i >> 24 & 255);
			((Buffer) buf).flip();
			return buf;
		} catch (IOException e) {
			ClientLogger.display(e);
			return null;
		}
	}
}
