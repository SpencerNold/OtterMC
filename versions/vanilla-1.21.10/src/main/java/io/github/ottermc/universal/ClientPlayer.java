package io.github.ottermc.universal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ClientPlayer extends UPlayer {
    @Override
    public boolean _isSprintViable() {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity player = client.player;
        if (player == null)
            return false;
        if (player.horizontalCollision || player.isSneaking())
            return false;
        if (((LivingEntity) player).forwardSpeed <= 0)
            return false;
        if (((ClientPlayerEntity) player).input.getMovementInput().length() <= 1e-5f)
            return false;
        return true;
    }

    @Override
    public void _setSprinting(boolean sprinting) {
        MinecraftClient client = MinecraftClient.getInstance();
        LivingEntity player = client.player;
        if (player == null)
            return;
        player.setSprinting(sprinting);
    }
}
