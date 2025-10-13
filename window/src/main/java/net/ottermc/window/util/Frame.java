package net.ottermc.window.util;

import net.ottermc.window.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Frame {

    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, Font> fontCache = new HashMap<>();

    public static Image loadImage(String name) {
        if (imageCache.containsKey(name))
            return imageCache.get(name);
        Image image = null;
        try {
            InputStream input = Main.class.getResourceAsStream(name);
            if (input != null) {
                image = ImageIO.read(input);
                input.close();
            }
        } catch (IOException e) {
            return null;
        }
        imageCache.put(name, image);
        return image;
    }

    public static Font loadFont(String name) {
        if (fontCache.containsKey(name))
            return fontCache.get(name);
        Font font = null;
        try {
            InputStream input = Main.class.getResourceAsStream(name);
            if (input != null) {
                font = Font.createFont(Font.TRUETYPE_FONT, input);
                input.close();
            }
        } catch (IOException | FontFormatException e) {
            return null;
        }
        fontCache.put(name, font);
        return font;
    }
}
