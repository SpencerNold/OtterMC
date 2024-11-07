package io.github.ottermc.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.TextureUtil;

public class GenericImageObject {

	protected final int texId;
	
	public GenericImageObject(Class<?> clazz, String name) throws IOException {
		texId = TextureUtil.glGenTextures();
		InputStream stream = clazz.getResourceAsStream(name);
		BufferedImage image = ImageIO.read(stream);
		TextureUtil.uploadTextureImageAllocate(texId, image, true, true);
	}
	
	public int getTextureId() {
		return texId;
	}
	
	public static GenericImageObject getGenericIgnoreException(Class<?> clazz, String name) {
		try {
			return new GenericImageObject(clazz, name);
		} catch (Exception e) {
			return null;
		}
	}
}
