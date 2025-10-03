package io.github.ottermc.screen;

public abstract class UniversalFontRenderer  {

    private static UniversalFontRenderer instance;

    public static void register(UniversalFontRenderer instance) {
        UniversalFontRenderer.instance = instance;
    }

    public static boolean isCharSupported(char c) {
        if (instance == null)
            throw new IllegalStateException("UniversalFontRenderer must be registered before it can be used");
        return instance.isCharacterSupportedI(c);
    }

    public static float getStringWidth(String text) {
        if (instance == null)
            throw new IllegalStateException("UniversalFontRenderer must be registered before it can be used");
        return instance.getStringWidthI(text);
    }

    public static float getStringHeight() {
        if (instance == null)
            throw new IllegalStateException("UniversalFontRenderer must be registered before it can be used");
        return instance.getStringHeightI();
    }

    public static void drawText(String text, float x, float y, int color) {
        if (instance == null)
            throw new IllegalStateException("UniversalFontRenderer must be registered before it can be used");
        instance.drawTextI(text, x, y, color);
    }

    protected final Font font;
    protected final Charset charset;

    public UniversalFontRenderer(Font font, Charset charset) {
        this.font = font;
        this.charset = charset;
    }

    public abstract boolean isCharacterSupportedI(char c);
    public abstract float getStringWidthI(String text);
    public abstract float getStringHeightI();
    public abstract void drawTextI(String text, float x, float y, int color);
}
