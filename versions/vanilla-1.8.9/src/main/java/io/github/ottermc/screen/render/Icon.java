package io.github.ottermc.screen.render;

import io.github.ottermc.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Icon {

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
			Logger.error(e);
			return null;
		}
	}
}
