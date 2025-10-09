package io.github.ottermc.screen.hud;

import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public abstract class Component {

    protected final DrawableHelper drawable = new DrawableHelper();

    protected final int defaultX, defaultY, width, height;

    protected boolean visible = false;

    public Component(int defaultX, int defaultY, int width, int height) {
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.width = width;
        this.height = height;
    }

    protected abstract void draw(Minecraft mc, GuiIngame gui, float partialTicks);

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
