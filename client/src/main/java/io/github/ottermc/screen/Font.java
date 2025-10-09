package io.github.ottermc.screen;

public abstract class Font {

    protected final int textureId;

    protected Font(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }
}
