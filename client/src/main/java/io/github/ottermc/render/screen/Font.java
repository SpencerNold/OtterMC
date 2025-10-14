package io.github.ottermc.render.screen;

public abstract class Font {

    protected final int textureId;

    protected Font(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }
}
