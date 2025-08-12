package io.github.ottermc.smp.modules.game;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import me.spencernold.transformer.Reflection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class FishingHelper extends Module implements RunTickListener {

    private final String fishingBobberEntityClassName = FishingBobberEntity.class.getName().replace('.', '/');

    private int skipTickTimer0 = 4;
    private int skipTickTimer1 = 4;

    public FishingHelper() {
        super("FishingHelper", Category.GAME);
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
        if (client.world == null || client.player == null || client.interactionManager == null)
            return;
        ItemStack itemStack = ((PlayerEntity) client.player).getInventory().getMainStacks().getFirst();
        if (itemStack == null || itemStack.getItem() != Items.FISHING_ROD)
            return;
        FishingBobberEntity bobber = ((PlayerEntity) client.player).fishHook;
        if (bobber != null) {
            boolean caught = (boolean) Reflection.getValue(fishingBobberEntityClassName, bobber, "caughtFish");
            if (caught) {
                if (skipTickTimer0 > 0)
                    skipTickTimer0--;
                else {
                    client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                    skipTickTimer0 = 4;
                }
            }
        } else {
            if (skipTickTimer1 > 0)
                skipTickTimer1--;
            else {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                skipTickTimer1 = 1;
            }
        }
    }

    @Override
    public Object getIcon() {
        return null;
    }
}
