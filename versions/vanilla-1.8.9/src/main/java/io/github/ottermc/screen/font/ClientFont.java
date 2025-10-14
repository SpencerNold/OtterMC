package io.github.ottermc.screen.font;

import io.github.ottermc.render.screen.Font;
import net.minecraft.client.renderer.texture.TextureUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ClientFont extends Font {

	public ClientFont(String name) throws IOException {
		super(TextureUtil.glGenTextures());
		InputStream input = getClass().getResourceAsStream(name);
		if (input != null) {
			BufferedImage image = ImageIO.read(input);
			TextureUtil.uploadTextureImageAllocate(textureId, image, true, true);
			input.close();
		}
	}

	public static ClientFont getFontIgnoreException(String name) {
		try {
			return new ClientFont(name);
		} catch (Exception e) {
			return null;
		}
	}
}
