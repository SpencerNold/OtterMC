package io.github.ottermc.render;

public class Color {
	
	private static final Color DEFAULT = new Color(48 + 48, 25 + 48, 52 + 48, 255);
    public static final Color WHITE = new Color(-1);

	private final int value;
    private final boolean hasAlpha;

	public Color(int r, int g, int b, int a) {
        this(((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF), true);
	}

	public Color(int rgb) {
		this.value = 0xff000000 | rgb;
        this.hasAlpha = false;
	}
	
    public Color(int value, boolean hasAlpha) {
        this.hasAlpha = hasAlpha;
        this.value = hasAlpha ? value : (0xFF000000 | value);
    }

	public Color(float r, float g, float b, float a) {
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
	}
	
    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    public int getBlue() {
        return (value) & 0xFF;
    }

    public int getAlpha() {
        return (value >> 24) & 0xff;
    }
    
    public float getRedNormal() {
    	return getRed() / 255.0f;
    }
    
    public float getGreenNormal() {
    	return getGreen() / 255.0f;
    }
    
    public float getBlueNormal() {
    	return getBlue() / 255.0f;
    }
    
    public float getAlphaNormal() {
    	return getAlpha() / 255.0f;
    }
    
    public int getValue() {
		return value;
	}
    
    public int getValue(int alpha) {
    	return (value & 0x00FFFFFF) | (alpha << 24); 
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

    public static Color getDefault() {
        return DEFAULT;
    }
}
