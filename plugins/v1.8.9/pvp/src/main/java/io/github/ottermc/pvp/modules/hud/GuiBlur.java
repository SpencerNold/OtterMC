package io.github.ottermc.pvp.modules.hud;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.Module;
import io.github.ottermc.pvp.listeners.DrawDefaultBackgroundListener;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.screen.render.BlurShaderProgram;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiBlur extends Module implements RunTickListener, DrawDefaultBackgroundListener {

    public GuiBlur() {
        super("Gui Blur", CategoryList.DISPLAY);
    }

    @Override
    public void onEnable() {
        EventBus.add(this);
    }

    @Override
    public void onDisable() {
        EventBus.remove(this);
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null && BlurShaderProgram.isActive())
            BlurShaderProgram.setActive(false);
    }

    @Override
    public void onDrawDefaultBackground(DrawDefaultBackgroundEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null) {
            BlurShaderProgram.setActive(true);
            GuiScreen gui = (GuiScreen) event.getGui();
            drawGradientRect(gui, 0, 0, gui.width, gui.height, 0x70101010, 0x80101010);
        }
        event.setCanceled(true);
    }

    private void drawGradientRect(GuiScreen gui, int x, int y, int width, int height, int c1, int c2) {
        Reflection.call("net/minecraft/client/gui/Gui", gui, "drawGradientRect", "(IIIIII)V", x, y, width, height, c1, c2);
    }

    @Override
    public String getDescription() {
        return "Blurs the background of GUIs";
    }
}
