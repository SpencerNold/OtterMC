package io.github.ottermc.screen.font;

import java.io.IOException;

import io.github.ottermc.render.GenericImageObject;

public class Font extends GenericImageObject {
	
	public Font(String name) throws IOException {
		super(Font.class, name);
	}

	public static Font getFontIgnoreException(String name) {
		try {
			return new Font(name);
		} catch (Exception e) {
			return null;
		}
	}
}
