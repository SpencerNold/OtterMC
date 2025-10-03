package io.github.ottermc.screen.font;

import net.minecraft.client.renderer.texture.TextureUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Font {

	private final int texId;

	public Font(String name) throws IOException {
		texId = TextureUtil.glGenTextures();
		InputStream input = getClass().getResourceAsStream(name);
		if (input != null) {
			BufferedImage image = ImageIO.read(input);
			TextureUtil.uploadTextureImageAllocate(texId, image, true, true);
			input.close();
		}
	}

	public int getTextureId() {
		return texId;
	}

	public static Font getFontIgnoreException(String name) {
		try {
			return new Font(name);
		} catch (Exception e) {
			return null;
		}
	}
}
