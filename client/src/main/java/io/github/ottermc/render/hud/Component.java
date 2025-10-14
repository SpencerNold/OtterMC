package io.github.ottermc.render.hud;

public abstract class Component {

    protected final int defaultX, defaultY, width, height;
    protected boolean visible = false;

    public Component(int defaultX, int defaultY, int width, int height) {
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Object context);

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getDefaultX() {
        return defaultX;
    }

    public int getDefaultY() {
        return defaultY;
    }

    public int getRawWidth() {
        return width;
    }

    public int getRawHeight() {
        return height;
    }
}
