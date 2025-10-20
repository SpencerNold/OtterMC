package io.github.ottermc.universal;

import io.github.ottermc.render.screen.AbstractScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class ClientAbstractScreen extends GuiScreen {

    private final AbstractScreen screen;

    public ClientAbstractScreen(AbstractScreen screen) {
        this.screen = screen;
    }

    @Override
    public void initGui() {
        BlurShaderProgram.setActive(true, true);
        screen.open();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        screen.draw(new DrawableHelper(), mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        screen.mouseClicked(mouseButton, mouseX, mouseY, false);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        screen.mouseReleased(state, mouseX, mouseY);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        screen.mouseDragged(clickedMouseButton, mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int dx = Mouse.getEventDWheel();
        if (dx != 0)
            screen.mouseScrolled(Mouse.getX(), Mouse.getY(), dx, dx);
        super.handleMouseInput();
    }

    @Override
    public void updateScreen() {
        screen.tick();
    }

    @Override
    public void onGuiClosed() {
        screen.close();
        BlurShaderProgram.setActive(false, false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
