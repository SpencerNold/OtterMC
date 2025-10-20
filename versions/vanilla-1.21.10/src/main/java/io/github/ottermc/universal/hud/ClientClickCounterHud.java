package io.github.ottermc.universal.hud;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.ClickMouseListener;
import io.github.ottermc.events.listeners.RightClickMouseListener;
import io.github.ottermc.render.hud.impl.ClickCounterHud;
import io.github.ottermc.universal.Type;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public class ClientClickCounterHud extends ClickCounterHud implements ClickMouseListener, RightClickMouseListener {

    private static final ClickCounter LEFT, RIGHT;

    static {
        LEFT = new ClickCounter();
        RIGHT = new ClickCounter();
    }

    public ClientClickCounterHud() {
        super(60, 19);
        EventBus.add(this);
    }

    @Override
    public void draw(@Type(DrawContext.class) Object context) {
        drawCounter((DrawContext) context, MinecraftClient.getInstance());
    }

    @Override
    public void drawDummyObject(@Type(DrawContext.class) Object context) {
        drawCounter((DrawContext) context, MinecraftClient.getInstance());
    }

    private void drawCounter(DrawContext drawable, MinecraftClient mc) {
        int color = io.github.ottermc.modules.impl.hud.ClickCounter.getColor().getValue();
        draw(LEFT, mc, mc.options.attackKey, drawable, getDefaultX(), getDefaultY(), 29, getRawHeight(), color);
        draw(RIGHT, mc, mc.options.useKey, drawable, getDefaultX() + 31, getDefaultY(), 29, getRawHeight(), color);
    }

    private void draw(ClickCounter counter, MinecraftClient mc, KeyBinding binding, DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, (binding.isPressed() && mc.currentScreen == null) ? 0x66ffffff : 0x90000000);
        String text = counter.toString();
        context.drawText(mc.textRenderer, text, (int) (x + 14.5f - mc.textRenderer.getWidth(text) * 0.5f) + 1, (int) (y + 5.5f) + 1, color, false);
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
