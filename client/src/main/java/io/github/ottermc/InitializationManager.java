package io.github.ottermc;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.PostInitializeListener;
import io.github.ottermc.events.listeners.RunTickListener;

import java.io.IOException;

public class InitializationManager implements PostInitializeListener, RunTickListener {

    private final Object client;
    private boolean hasPostInitialized;
    private int timer;

    public InitializationManager(Object client) {
        this.client = client;
        this.hasPostInitialized = false;
        this.timer = 20;
    }

    @Override
    public void onPostInitializeListener(PostInitializeEvent event) {
        attemptPostInitializeProcess();
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        if (hasPostInitialized) {
            EventBus.remove(this);
            return;
        }
        if (timer <= 0) {
            attemptPostInitializeProcess();
            timer = 20;
            return;
        }
        timer--;
    }

    private void attemptPostInitializeProcess() {
        AbstractSubClient client = AbstractSubClient.instance;
        if (client == null)
            return;
        client.onPostInit();
        try {
            client.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hasPostInitialized = true;
    }
}
