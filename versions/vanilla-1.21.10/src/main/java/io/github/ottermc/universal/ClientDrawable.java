package io.github.ottermc.universal;

import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;

public class ClientDrawable extends UDrawable {
    @Override
    protected void _push(Object context) {
        getMatrices(context).pushMatrix();
    }

    @Override
    protected void _pop(Object context) {
        getMatrices(context).popMatrix();
    }

    @Override
    protected void _translate(Object context, float x, float y) {
        getMatrices(context).translate(x, y);
    }

    @Override
    protected void _scale(Object context, float x, float y) {
        getMatrices(context).scale(x, y);
    }

    private Matrix3x2fStack getMatrices(Object context) {
        DrawContext ctx = (DrawContext) context;
        return ctx.getMatrices();
    }
}
