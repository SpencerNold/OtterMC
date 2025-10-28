package io.github.ottermc.universal;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ClientPlayer extends UPlayer {
    @Override
    public boolean _isSprintViable() {
        Minecraft mc = Minecraft.getMinecraft();
        Entity player = mc.thePlayer;
        if (player == null)
            return false;
        if (player.isCollidedHorizontally || player.isSneaking())
            return false;
        if (((EntityLivingBase) player).moveForward <= 0)
            return false;
        return true;
    }

    @Override
    public void _setSprinting(boolean sprinting) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase player = mc.thePlayer;
        if (player == null)
            return;
        player.setSprinting(sprinting);
    }
}
