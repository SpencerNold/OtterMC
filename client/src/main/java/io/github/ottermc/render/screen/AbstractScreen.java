package io.github.ottermc.render.screen;

public abstract class AbstractScreen {

    public void open() {}
    public abstract void draw(Object context, int mouseX, int mouseY, float partialTicks);
    public void mouseClicked(int button, float mouseX, float mouseY, boolean doubled) {}
    public void mouseReleased(int button, float mouseX, float mouseY) {}
    public void mouseDragged(int button, float mouseX, float mouseY) {}
    public void mouseScrolled(float mouseX, float mouseY, double horizontalAmount, double verticalAmount) {}
    public void tick() {}
    public void close() {}

    public boolean intersects(float x, float y, int width, int height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
    }
}
