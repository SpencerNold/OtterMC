package io.github.fabric.universal.hud;

import io.github.ottermc.modules.impl.hud.Coordinate;
import io.github.ottermc.render.hud.impl.CoordinateHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;

public class ClientCoordinateHud extends CoordinateHud {
    @Override
    public void draw(Object context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Entity player = mc.player;
        if (player == null)
            return;
        String text = String.format("(%.1f, %.1f, %.1f [%c])", player.getX(), player.getY(), player.getZ(), Direction.fromHorizontalDegrees(player.getYaw()).name().toUpperCase().charAt(0));
        drawString((DrawContext) context, mc, text);
    }

    @Override
    public void drawDummyObject(Object context) {
        String text = "(XX.X, YY.Y, ZZ.Z [D])";
        drawString((DrawContext) context, MinecraftClient.getInstance(), text);
    }

    private void drawString(DrawContext context, MinecraftClient mc, String text) {
        int color = Coordinate.getColor().getValue();
        context.drawText(mc.textRenderer, text, getDefaultX(), getDefaultY(), color, false);
    }
}
