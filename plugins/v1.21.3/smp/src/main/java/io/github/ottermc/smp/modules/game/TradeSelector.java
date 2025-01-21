package io.github.ottermc.smp.modules.game;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOffer;

public class TradeSelector extends Module implements RunTickListener {

    public TradeSelector() {
        super("Trade Selector", Category.WORLD);
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.targetedEntity != null) {
            if (client.targetedEntity.getType() == EntityType.VILLAGER) {
                VillagerEntity entity = (VillagerEntity) client.targetedEntity;
            }
        }
    }

    @Override
    public Object getIcon() {
        return null;
    }
}
