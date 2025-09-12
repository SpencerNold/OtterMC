package io.github.ottermc.screen.hud;

import io.github.ottermc.listeners.RenderGameOverlayListener;
import io.github.ottermc.screen.render.BlurShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;

public class HudManager implements RenderGameOverlayListener {

    private final LinkedList<Component> components = new LinkedList<>();

    public void register(Component component) {
        components.add(component);
    }

    public LinkedList<Component> getComponents() {
        return components;
    }

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
        for (Component c : components) {
            if (!c.isVisible())
                continue;
            if (c instanceof MovableComponent) {
                ((MovableComponent) c).enableTranslate();
                c.draw(mc, (GuiIngame) event.getGuiIngame(), event.getPartialTicks());
                ((MovableComponent) c).disableTranslate();
            } else
                c.draw(mc, (GuiIngame) event.getGuiIngame(), event.getPartialTicks());
        }
        if (tex2d)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        else
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableBlend();
    }
}
