package io.github.ottermc.universal;

import io.github.ottermc.render.screen.AbstractScreen;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ClientDrawable extends UDrawable {
    @Override
    protected void _push(Object context) {
        GlStateManager.pushMatrix();
    }

    @Override
    protected void _pop(Object context) {
        GlStateManager.popMatrix();
    }

    @Override
    protected void _translate(Object context, float x, float y) {
        GlStateManager.translate(x, y, 0);
    }

    @Override
    protected void _scale(Object context, float x, float y) {
        GlStateManager.scale(x, y, 0);
    }

    @Override
    protected void _color(float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
    }

    @Override
    protected void _fillRect(@Type(DrawableHelper.class) Object context, int x, int y, int width, int height, int color) {
        ((DrawableHelper) context).fillRectangle(x, y, width, height, color);
    }

    @Override
    protected void _outlineRect(Object context, int x, int y, int width, int height, int color) {
        ((DrawableHelper) context).drawRectangle(x, y, width, height, color);
    }

    @Override
    protected void _drawString(Object context, String text, int x, int y, int color, boolean shadow) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, shadow);
    }

    @Override
    protected int _getScaledHeight() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    @Override
    protected int _getScaledWidth() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    @Override
    protected float _getStringWidth(String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    @Override
    protected float _getStringHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    @Override
    protected void _display(AbstractScreen screen) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && mc.currentScreen == null)
            mc.displayGuiScreen(new ClientAbstractScreen(screen));
    }
}
