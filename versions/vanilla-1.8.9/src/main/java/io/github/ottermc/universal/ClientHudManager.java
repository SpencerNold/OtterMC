package io.github.ottermc.universal;

import io.github.ottermc.render.hud.Component;
import io.github.ottermc.render.hud.HudManager;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ClientHudManager extends HudManager {

    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (BlurShaderProgram.shouldHideHud())
            return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.showDebugInfo)
            return;
        mc.entityRenderer.setupOverlayRendering();
        boolean tex2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        super.onRenderGameOverlay(event);
        if (tex2d)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        else
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableBlend();
    }

    @Override
    protected void drawComponent(Object context, Component component) {
        component.draw(new DrawableHelper());
    }
}
