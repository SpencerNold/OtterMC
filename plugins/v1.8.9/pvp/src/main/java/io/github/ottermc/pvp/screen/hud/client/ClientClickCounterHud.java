package io.github.ottermc.pvp.screen.hud.client;

import io.github.ottermc.screen.hud.MovableComponent;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class ClientClickCounterHud extends MovableComponent {

    private static final ClickCounter LEFT;
    private static final ClickCounter RIGHT;

    static {
        Minecraft mc = Minecraft.getMinecraft();
        LEFT = new ClickCounter(mc.gameSettings.keyBindAttack);
        RIGHT = new ClickCounter(mc.gameSettings.keyBindUseItem);
    }

    public ClientClickCounterHud() {
        super(10, 10, 58, 18);
    }

    @Override
    protected void draw(Minecraft mc, GuiIngame gui, float partialTicks) {
        drawCounter(mc);
    }

    @Override
    public void drawDummyObject(Minecraft mc, Gui gui, float partialTicks) {
        drawCounter(mc);
    }

    private void drawCounter(Minecraft mc) {
        int color = io.github.ottermc.pvp.modules.hud.ClickCounter.getColor().getValue();
        LEFT.update();
        LEFT.draw(mc, drawable, getDefaultX(), getDefaultY(), 28, getRawHeight(), color);
        RIGHT.update();
        RIGHT.draw(mc, drawable, getDefaultX() + 30, getDefaultY(), 28, getRawHeight(), color);
    }

    @Override
    public int getSerialId() {
        return "CLICK_COUNTER_COMPONENT".hashCode();
    }

    public static void addLefClick() {
        // TODO Implement at some point
    }

    public static void addRightClick() {
        // TODO Implement at some point
    }

    private static class ClickCounter {

        final List<Long> clicks = new ArrayList<>();
        final KeyBinding key;
        boolean wasPressed;
        long lastPressed;

        ClickCounter(KeyBinding key) {
            this.key = key;
        }

        void draw(Minecraft mc, DrawableHelper drawable, int x, int y, int width, int height, int color) {
            drawable.fillRectangle(x, y, width, height, (key.isKeyDown() && mc.currentScreen == null) ? 0x66ffffff : 0x90000000);
            String text = this.toString();
            mc.fontRendererObj.drawString(text, x + 14.5f - mc.fontRendererObj.getStringWidth(text) * 0.5f, y + 5.5f, color, false);
        }

        void update() {
            if (Minecraft.getMinecraft().currentScreen != null)
                return;
            boolean pressed = key.isKeyDown();
            if (pressed != wasPressed) {
                lastPressed = System.currentTimeMillis();
                wasPressed = pressed;
                if (pressed)
                    clicks.add(lastPressed);
            }
        }

        int getClicks() {
            final long time = System.currentTimeMillis();
            clicks.removeIf(l -> l + 1000 < time);
            int count = clicks.size();
            return count >= 8 ? count + 1 : count;
        }

        public String toString() {
            return getClicks() < 10 ? "0" + getClicks() : getClicks() + "";
        }
    }
}
