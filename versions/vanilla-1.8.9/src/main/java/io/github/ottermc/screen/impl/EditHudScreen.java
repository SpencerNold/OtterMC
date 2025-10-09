package io.github.ottermc.screen.impl;

import io.github.ottermc.SubClient;
import io.github.ottermc.logging.Logger;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.ClientTheme;
import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.HudManager;
import io.github.ottermc.screen.hud.MovableComponent;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class EditHudScreen extends AbstractScreen {

    private final HudManager manager;

    private MovableComponent selected = null;
    private MovableComponent lastClicked = null;
    private int clickOffsX = 0, clickOffsY = 0;

    public EditHudScreen() {
        this.manager = SubClient.getInstance().getHudManager();
    }

    @Override
    public void onScreenOpen() {
        BlurShaderProgram.setActive(true, true);
    }

    @Override
    public void onScreenClose() {
        BlurShaderProgram.setActive(false, false);
        try {
            SubClient.getInstance().save();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public void renderScreen(int mouseX, int mouseY, float partialTicks) {
        handleSelectedMouseMovement(mouseX, mouseY);
        DrawableHelper drawable = getDrawable();
        for (Component c : manager.getComponents()) {
            if (!(c instanceof MovableComponent) || !c.isVisible())
                continue;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            MovableComponent component = (MovableComponent) c;
            component.enableTranslate();
            component.drawDummyObject(Minecraft.getMinecraft(), this, partialTicks);
            component.disableTranslate(); // TODO For now...
            float scale = component.getScale();
            int x = component.getDefaultX() + component.getXOffset();
            int y = component.getDefaultY() + component.getYOffset();
            int width = (int) (component.getRawWidth() * scale);
            int height = (int) (component.getRawHeight() * scale);
            drawable.drawRectangle(x - 2, y - 2, width + 4, height + 4, lastClicked == c ? ClientTheme.getColor().getValue() : 0xF5E4E4E4);
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) {
        selected = null;
        DrawableHelper drawable = getDrawable();
        for (Component c : manager.getComponents()) {
            if (c instanceof MovableComponent && c.isVisible()) {
                MovableComponent component = (MovableComponent) c;
                float scale = component.getScale();
                int x = (component.getDefaultX() + component.getXOffset());
                int y = (component.getDefaultY() + component.getYOffset());
                int width = (int) (component.getRawWidth() * scale);
                int height = (int) (component.getRawHeight() * scale);
                if (drawable.intersects(x - 2, y - 2, width + 4, height + 4, mouseX, mouseY)) {
                    selected = component;
                    clickOffsX = mouseX - (component.getDefaultX() + component.getXOffset());
                    clickOffsY = mouseY - (component.getDefaultY() + component.getYOffset());
                    break;
                }
            }
        }
        lastClicked = selected;
    }

    private void handleSelectedMouseMovement(int mouseX, int mouseY) {
        if (selected != null) {
            int x = (mouseX - clickOffsX - selected.getDefaultX());
            int y = (mouseY - clickOffsY - selected.getDefaultY());
            selected.setOffset(x, y);
            clampSelectedToScreen();
        }
    }

    private void clampSelectedToScreen() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = resolution.getScaledWidth();
        int screenHeight = resolution.getScaledHeight();

        float scale = selected.getScale();
        int width = (int) (selected.getRawWidth() * scale);
        int height = (int) (selected.getRawHeight() * scale);

        int x = selected.getDefaultX() + selected.getXOffset();
        int y = selected.getDefaultY() + selected.getYOffset();

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

    @Override
    public void onHandleMouseInput() {
        int dx = Mouse.getEventDWheel();
        if (dx != 0 && lastClicked != null) {
            float scale = lastClicked.getScale() + ((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.05f : 0.01f) * (dx < 0 ? -1 : 1));
            lastClicked.setScale(scale);
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        selected = null;
    }
}
