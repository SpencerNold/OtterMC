package io.github.ottermc.smp.modules.world;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.Listener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class LightLevel extends Module implements Listener {

    public LightLevel() {
        super("Light Level", Category.WORLD);
        setActive(true);
    }

    @Override
    public void onEnable() {
        EventBus.add(this);
    }

    @Override
    public void onDisable() {
        EventBus.remove(this);
    }

    private boolean canMobSpawn(ClientWorld world, BlockPos pos) {
        if (!SpawnRestriction.isSpawnPosAllowed(EntityType.CREEPER, world, pos))
            return false;

        return world.getLightLevel(LightType.BLOCK, pos) < 1;
    }

    @Override
    public Object getIcon() {
        return null;
    }
}
