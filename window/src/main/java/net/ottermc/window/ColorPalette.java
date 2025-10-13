package net.ottermc.window;

import java.awt.*;

public class ColorPalette {

    public static final ColorPalette DARK0 = new ColorPalette(0xFF121212);
    public static final ColorPalette DARK1 = new ColorPalette(0xFF202020);
    public static final ColorPalette ACCENT = new ColorPalette(0xFFE6D7F3);
    public static final ColorPalette TEXT = new ColorPalette(0xFF1B0A3E);
    public static final ColorPalette RED = new ColorPalette(0xFFC23B22);

    private final int argb;

    private ColorPalette(int argb) {
        this.argb = argb;
    }

    public ColorPalette haze(int alpha) {
        return new ColorPalette((argb & 0x00FFFFFF) | ((alpha & 0xFF) << 24));
    }

    public ColorPalette lighter(int amount) {
        return new ColorPalette(argb + (0x00010101 * amount));
    }

    public Color getColor() {
        return new Color(argb, true);
    }
}
