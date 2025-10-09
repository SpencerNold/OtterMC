package io.github.ottermc.hud.client;

import io.github.ottermc.modules.hud.Coordinate;
import io.github.ottermc.screen.hud.MovableComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class ClientCoordinateHud extends MovableComponent {

    public ClientCoordinateHud() {
        super(10, 10, 98, 7);
    }

    @Override
    protected void draw(Minecraft mc, GuiIngame gui, float partialTicks) {
        Entity player = mc.thePlayer;
        String text = String.format("(%.1f, %.1f, %.1f [%c])", player.posX, player.posY, player.posZ, EnumFacing.fromAngle(player.rotationYaw).getName().toUpperCase().charAt(0));
        drawString(mc, text);
    }

    @Override
    public void drawDummyObject(Minecraft mc, Gui gui, float partialTicks) {
        String text = "(XX.X, YY.Y, ZZ.Z [D])";
        drawString(mc, text);
    }

    private void drawString(Minecraft mc, String text) {
        int color = Coordinate.getColor().getValue();
        mc.fontRendererObj.drawString(text, getDefaultX(), getDefaultY(), color);
    }

    @Override
    public int getSerialId() {
        return "COORDINATE_COMPONENT".hashCode();
    }
}
