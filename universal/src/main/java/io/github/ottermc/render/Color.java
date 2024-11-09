package io.github.ottermc.render;

// Ripped straight from java.awt.Color with some minor alterations

// I'm not sure if stripped JVM implementations
// contain the Java standard library class or not
// so just to be safe, yoink!
public class Color {
	
	public static final Color DEFAULT = new Color(0x5A2D81, false);

	private int value;

	public Color(int r, int g, int b, int a) {
		value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}

	public Color(int rgb) {
		value = 0xff000000 | rgb;
	}
	
    public Color(int rgba, boolean hasalpha) {
        if (hasalpha) {
            value = rgba;
        } else {
            value = 0xFF000000 | rgba;
        }
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
        return (value >> 0) & 0xFF;
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
}
