package ottermc.v1218;

import io.github.ottermc.render.screen.AbstractScreen;
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        screen.mouseClicked(button, (float) mouseX, (float) mouseY, false);
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        screen.mouseReleased(button, (float) mouseX, (float) mouseY);
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        screen.mouseDragged(button, (float) mouseX, (float) mouseY);
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
