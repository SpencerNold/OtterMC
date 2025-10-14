package io.github.ottermc.universal.hud;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.ClickMouseListener;
import io.github.ottermc.events.listeners.RightClickMouseListener;
import io.github.ottermc.render.hud.impl.ClickCounterHud;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.universal.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class ClientClickCounterHud extends ClickCounterHud implements ClickMouseListener, RightClickMouseListener {

    private static final ClickCounter LEFT, RIGHT;

    static {
        LEFT = new ClickCounter();
        RIGHT = new ClickCounter();
    }

    public ClientClickCounterHud() {
        super();
        EventBus.add(this);
    }

    @Override
    public void draw(@Type(DrawableHelper.class) Object context) {
        drawCounter((DrawableHelper) context, Minecraft.getMinecraft());
    }

    @Override
    public void drawDummyObject(@Type(DrawableHelper.class) Object context) {
        drawCounter((DrawableHelper) context, Minecraft.getMinecraft());
    }

    private void drawCounter(DrawableHelper drawable, Minecraft mc) {
        int color = io.github.ottermc.modules.impl.hud.ClickCounter.getColor().getValue();
        draw(LEFT, mc, mc.gameSettings.keyBindAttack, drawable, getDefaultX(), getDefaultY(), 28, getRawHeight(), color);
        draw(RIGHT, mc, mc.gameSettings.keyBindUseItem, drawable, getDefaultX() + 30, getDefaultY(), 28, getRawHeight(), color);
    }

    private void draw(ClickCounter counter, Minecraft mc, KeyBinding binding, DrawableHelper drawable, int x, int y, int width, int height, int color) {
        drawable.fillRectangle(x, y, width, height, (binding.isKeyDown() && mc.currentScreen == null) ? 0x66ffffff : 0x90000000);
        String text = counter.toString();
        mc.fontRendererObj.drawString(text, x + 14.5f - mc.fontRendererObj.getStringWidth(text) * 0.5f, y + 5.5f, color, false);
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

}
