package io.github.ottermc.pvp.screen.hud.client;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.ClickMouseListener;
import io.github.ottermc.pvp.listeners.RightClickMouseListener;
import io.github.ottermc.screen.hud.MovableComponent;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class ClientClickCounterHud extends MovableComponent implements ClickMouseListener, RightClickMouseListener {

    private static final ClickCounter LEFT, RIGHT;

    static {
        Minecraft mc = Minecraft.getMinecraft();
        LEFT = new ClickCounter(mc.gameSettings.keyBindAttack);
        RIGHT = new ClickCounter(mc.gameSettings.keyBindUseItem);
    }

    public ClientClickCounterHud() {
        super(10, 10, 58, 18);
        EventBus.add(this);
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
        LEFT.draw(mc, drawable, getDefaultX(), getDefaultY(), 28, getRawHeight(), color);
        RIGHT.draw(mc, drawable, getDefaultX() + 30, getDefaultY(), 28, getRawHeight(), color);
    }

    @Override
    public int getSerialId() {
        return "CLICK_COUNTER_COMPONENT".hashCode();
    }

    @Override
    public void onClickMouse(ClickMouseEvent event) {
        if (visible)
            addLeftClick();
    }

    @Override
    public void onRightClickMouse(RightClickMouseEvent event) {
        if (visible)
            addRightClick();
    }

    public static void addLeftClick() {
        LEFT.clicks.add(System.currentTimeMillis());
    }

    public static void addRightClick() {
        RIGHT.clicks.add(System.currentTimeMillis());
    }

    private static class ClickCounter {

        private final List<Long> clicks = new ArrayList<>();
        private final KeyBinding key;

        private ClickCounter(KeyBinding key) {
            this.key = key;
        }

        void draw(Minecraft mc, DrawableHelper drawable, int x, int y, int width, int height, int color) {
            drawable.fillRectangle(x, y, width, height, (key.isKeyDown() && mc.currentScreen == null) ? 0x66ffffff : 0x90000000);
            String text = this.toString();
            mc.fontRendererObj.drawString(text, x + 14.5f - mc.fontRendererObj.getStringWidth(text) * 0.5f, y + 5.5f, color, false);
        }

        int getClicks() {
            final long time = System.currentTimeMillis();
            clicks.removeIf(l -> l + 1000 < time);
            return clicks.size();
        }

        public String toString() {
            return getClicks() < 10 ? "0" + getClicks() : getClicks() + "";
        }
    }
}
