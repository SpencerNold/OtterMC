package io.github.fabric.universal;

import io.github.ottermc.render.screen.AbstractScreen;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClientAbstractScreen extends Screen {

    private final AbstractScreen screen;

    public ClientAbstractScreen(AbstractScreen screen) {
        super(Text.literal("ClientAbstractScreen"));
        this.screen = screen;
    }

    @Override
    protected void init() {
        screen.open();
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        screen.draw(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        screen.mouseClicked(click.button(), (float) click.x(), (float) click.y(), doubled);
        return false;
    }

    @Override
    public boolean mouseReleased(Click click) {
        screen.mouseReleased(click.button(), (float) click.x(), (float) click.y());
        return false;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        screen.mouseDragged(click.button(), (float) click.x(), (float) click.y());
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        screen.mouseScrolled((float) mouseX, (float) mouseY, horizontalAmount, verticalAmount);
        return false;
    }

    @Override
    public void tick() {
        screen.tick();
    }

    @Override
    public void removed() {
        screen.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
