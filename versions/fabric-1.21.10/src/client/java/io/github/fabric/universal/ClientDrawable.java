package io.github.fabric.universal;

import io.github.ottermc.render.screen.AbstractScreen;
import io.github.ottermc.universal.Type;
import io.github.ottermc.universal.UDrawable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;

public class ClientDrawable extends UDrawable {
    @Override
    protected void _push(@Type(DrawContext.class) Object context) {
        getMatrices(context).pushMatrix();
    }

    @Override
    protected void _pop(@Type(DrawContext.class) Object context) {
        getMatrices(context).popMatrix();
    }

    @Override
    protected void _translate(@Type(DrawContext.class) Object context, float x, float y) {
        getMatrices(context).translate(x, y);
    }

    @Override
    protected void _scale(@Type(DrawContext.class) Object context, float x, float y) {
        getMatrices(context).scale(x, y);
    }

    @Override
    protected void _color(float r, float g, float b, float a) {
        // Unsupported
    }

    @Override
    protected void _fillRect(@Type(DrawContext.class) Object context, int x, int y, int width, int height, int color) {
        ((DrawContext) context).fill(x, y, x + width, y + height, color);
    }

    @Override
    protected void _outlineRect(Object context, int x, int y, int width, int height, int color) {
        DrawContext ctx = (DrawContext) context;
        ctx.fill(x, y, x + width, y + 1, color);
        ctx.fill(x, y + height - 1, x + width, y + height, color);
        ctx.fill(x, y + 1, x + 1, y + height - 1, color);
        ctx.fill(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    @Override
    protected void _drawString(Object context, String text, int x, int y, int color, boolean shadow) {
        ((DrawContext) context).drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, shadow);
    }

    @Override
    protected void _display(AbstractScreen screen) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.currentScreen == null)
            mc.setScreen(new ClientAbstractScreen(screen));
    }

    @Override
    protected int _getScaledHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    @Override
    protected int _getScaledWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    @Override
    protected float _getStringWidth(String text) {
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    @Override
    protected float _getStringHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    private Matrix3x2fStack getMatrices(Object context) {
        DrawContext ctx = (DrawContext) context;
        return ctx.getMatrices();
    }
}
