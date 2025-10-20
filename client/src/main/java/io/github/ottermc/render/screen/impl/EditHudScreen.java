package io.github.ottermc.render.screen.impl;

import io.github.ottermc.Game;
import io.github.ottermc.logging.Logger;
import io.github.ottermc.render.ClientTheme;
import io.github.ottermc.render.hud.Component;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.render.hud.MovableComponent;
import io.github.ottermc.render.screen.AbstractScreen;
import io.github.ottermc.universal.UDrawable;
import io.github.ottermc.universal.UKeyRegistry;
import io.github.ottermc.universal.UKeyboard;

import java.io.IOException;

public class EditHudScreen extends AbstractScreen {

    private final HudManager manager;
    private MovableComponent selected = null;
    private MovableComponent lastClicked = null;
    private float clickOffsX = 0.0f, clickOffsY = 0.0f;

    public EditHudScreen() {
        this.manager = Game.game.getHudManager();
    }

    @Override
    public void draw(Object context, int mouseX, int mouseY, float partialTicks) {
        handleSelectedMouseMovement(mouseX, mouseY);
        int color = ClientTheme.getColor().getValue();
        for (Component c : manager.getComponents()) {
            if (!(c instanceof MovableComponent) || !c.isVisible())
                continue;
            UDrawable.color(1.0f, 1.0f, 1.0f, 1.0f);
            MovableComponent component = (MovableComponent) c;
            component.enableTranslate(context);
            component.drawDummyObject(context);
            component.disableTranslate(context);
            drawScaleHint(context, component);
            float scale = component.getScale();
            float x = component.getDefaultX() + component.getXOffset();
            float y = component.getDefaultY() + component.getYOffset();
            int width = (int) (component.getRawWidth() * scale);
            int height = (int) (component.getRawHeight() * scale);
            UDrawable.outlineRect(context,(int) x - 2, (int) y - 2, width + 4, height + 4, lastClicked == c ? color : 0xF5E4E4E4);;
        }
    }

    @Override
    public void mouseClicked(int button, float mouseX, float mouseY, boolean doubled) {
        selected = null;
        for (Component c : manager.getComponents()) {
            if (c instanceof MovableComponent && c.isVisible()) {
                MovableComponent component = (MovableComponent) c;
                float scale = component.getScale();
                float x = (component.getDefaultX() + component.getXOffset());
                float y = (component.getDefaultY() + component.getYOffset());
                int width = (int) (component.getRawWidth() * scale);
                int height = (int) (component.getRawHeight() * scale);
                if (intersects(x - 2, y - 2, width + 4, height + 4, mouseX, mouseY)) {
                    selected = component;
                    clickOffsX = mouseX - (component.getDefaultX() + component.getXOffset());
                    clickOffsY = mouseY - (component.getDefaultY() + component.getYOffset());
                    break;
                }
            }
        }
        lastClicked = selected;
    }

    @Override
    public void mouseReleased(int button, float mouseX, float mouseY) {
        selected = null;
    }

    private void handleSelectedMouseMovement(int mouseX, int mouseY) {
        if (selected != null) {
            float x = (mouseX - clickOffsX - selected.getDefaultX());
            float y = (mouseY - clickOffsY - selected.getDefaultY());
            selected.setOffset(x, y);
            clampSelectedToScreen();
        }
    }

    private void clampSelectedToScreen() {
        int screenWidth = UDrawable.getScaledWidth();
        int screenHeight = UDrawable.getScaledHeight();

        float scale = selected.getScale();
        int width = (int) (selected.getRawWidth() * scale);
        int height = (int) (selected.getRawHeight() * scale);

        float x = selected.getDefaultX() + selected.getXOffset();
        float y = selected.getDefaultY() + selected.getYOffset();

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if ((x + width) > screenWidth)
            x = screenWidth - width;
        if ((y + height) > screenHeight)
            y = screenHeight - height;

        x -= selected.getDefaultX();
        y -= selected.getDefaultY();

        selected.setOffset(x, y);
    }

    private void drawScaleHint(Object context, MovableComponent component) {
        float x = component.getDefaultX() + component.getXOffset();
        float y = component.getDefaultY() + component.getYOffset();
        float height = component.getRawHeight() * component.getScale();

        int boxX = (int) x;
        int boxY = ((int) (y + height)) + 2;
        int boxWidth = 33;
        int boxHeight = 15;
        UDrawable.fillRect(context, boxX, boxY, boxWidth, boxHeight, 0x90000000);
        String text = String.valueOf((int) (component.getScale() * 100)) + "%";
        UDrawable.drawString(context, text, (int) (boxX + (boxWidth / 2.0f) - (UDrawable.getStringWidth(text) / 2.0f)), boxY + 4, -1, false);
    }

    @Override
    public void mouseScrolled(float mouseX, float mouseY, double horizontalAmount, double verticalAmount) {
        float scale = lastClicked.getScale() + ((UKeyboard.isKeyDown(UKeyRegistry.getKeyLShift()) ? 0.05f : 0.01f) * (verticalAmount < 0 ? -1 : 1));
        lastClicked.setScale(scale);
    }

    @Override
    public void close() {
        try {
            Game.game.save();
        } catch (IOException e) {
            Logger.error(e);
        }
    }
}
