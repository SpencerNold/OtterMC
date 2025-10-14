package io.github.ottermc.universal.hud;

import io.github.ottermc.modules.impl.hud.Coordinate;
import io.github.ottermc.render.hud.impl.CoordinateHud;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.universal.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class ClientCoordinateHud extends CoordinateHud {

    @Override
    public void draw(@Type(DrawableHelper.class) Object context) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity player = mc.thePlayer;
        String text = String.format("(%.1f, %.1f, %.1f [%c])", player.posX, player.posY, player.posZ, EnumFacing.fromAngle(player.rotationYaw).getName().toUpperCase().charAt(0));
        drawString(mc, text);
    }

    @Override
    public void drawDummyObject(@Type(DrawableHelper.class) Object context) {
        String text = "(XX.X, YY.Y, ZZ.Z [D])";
        drawString(Minecraft.getMinecraft(), text);
    }

    private void drawString(Minecraft mc, String text) {
        int color = Coordinate.getColor().getValue();
        mc.fontRendererObj.drawString(text, getDefaultX(), getDefaultY(), color);
    }
}
