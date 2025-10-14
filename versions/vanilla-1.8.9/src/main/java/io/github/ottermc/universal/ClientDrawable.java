package io.github.ottermc.universal;

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
}
