package io.github.ottermc.modules.impl.world;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.universal.UPlayer;

public class AutoSprint extends Module implements RunTickListener {

    public AutoSprint() {
        super("Auto Sprint", CategoryList.WORLD);
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
        if (UPlayer.isSprintViable())
            UPlayer.setSprinting(true);
    }
}
