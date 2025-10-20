package io.github.ottermc.screen.render;

import io.github.ottermc.render.screen.UniversalFontRenderer;
import io.github.ottermc.screen.font.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class DrawableHelper {

    public void setupOverlayRendering() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.clear(256);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, res.getScaledWidth_double(), res.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    public boolean isFontCharSupported(char c) {
        return UniversalFontRenderer.isCharSupported(c);
    }

    public void drawString(String text, int x, int y, float scale, int color) {
        float ratio = 0.25f * scale;
        float inv = 1.0f / ratio;
        x = (int) ((float) x * inv);
        y = (int) ((float) y * inv);
        GlStateManager.scale(ratio, ratio, ratio);
        FontRenderer.drawText(text, x, y, color);
        GlStateManager.scale(inv, inv, inv);
    }

    public void drawString(String text, int x, int y, int color) {
        drawString(text, x, y, 1.0f, color);
    }

    public int getStringWidth(String text, float scale) {
        return (int) (UniversalFontRenderer.getStringWidth(text) * (0.25f * scale));
    }

    public int getStringHeight(float scale) {
        return (int) (UniversalFontRenderer.getStringHeight() * (0.25f * scale));
    }

    public int getStringWidth(String text) {
        return getStringWidth(text, 1.0f);
    }

    public int getStringHeight() {
        return getStringHeight(1.0f);
    }

    public void fillRectangle(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public void drawRectangle(int x, int y, int width, int height, int color) {
        drawHorizontalLine(x, x + width, y, color);
        drawHorizontalLine(x, x + width, y + height - 1, color);
        drawVerticalLine(x, y + 1, y + height - 1, color);
        drawVerticalLine(x + width - 1, y + 1, y + height - 1, color);
    }

    public void outlineRectangle(int x, int y, int width, int height, int innerColor, int outerColor) {
        fillRectangle(x, y, width, height, innerColor);
        drawRectangle(x, y, width, height, outerColor);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, 100.0).tex((float) (textureX) * f, (float) (textureY + height) * f1).endVertex();
        renderer.pos(x + width, y + height, 100.0).tex((float) (textureX + width) * f, (float) (textureY + height) * f1).endVertex();
        renderer.pos(x + width, y, 100.0).tex((float) (textureX + width) * f, (float) (textureY) * f1).endVertex();
        renderer.pos(x, y, 100.0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
        tessellator.draw();
    }

    public double getWidth() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        return res.getScaledWidth_double();
    }

    public double getHeight() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        return res.getScaledHeight_double();
    }

    public boolean intersects(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
    }

    public int middle(int x1, int x2) {
        return (x1 / 2) - (x2 / 2);
    }

    public void drawHorizontalLine(int startX, int endX, int y, int color) {
        Gui.drawRect(startX, y, endX, y + 1, color);
    }

    public void drawVerticalLine(int x, int startY, int endY, int color) {
        Gui.drawRect(x, startY, x + 1, endY, color);
    }
}
