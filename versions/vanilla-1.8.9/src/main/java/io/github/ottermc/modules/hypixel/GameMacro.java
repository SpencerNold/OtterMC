package io.github.ottermc.modules.hypixel;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;

public class GameMacro extends HypixelModule implements RunTickListener {

    private static GameMacro instance;

    public GameMacro() { // hard code the commands, and add a 3 second delay timer
        super("Macros");
        instance = this;
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
        if (!isConnectedToHypixel())
            return;

    }

    public static boolean isModActive() {
        return instance.isActive();
    }
}
