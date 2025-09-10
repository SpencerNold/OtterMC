package io.github.ottermc;

import agent.Agent;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.PostInitializeListener;
import io.github.ottermc.events.listeners.RunTickListener;

import java.io.IOException;
import java.lang.reflect.Method;

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
        if (!Agent.isInjectionLoad())
            return;
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
        Agent.PLUGINS.forEach(((plugin, implementation) -> {
            implementation.onPostInit();
        }));
        postInitClient();
        try {
            Agent.getClient().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hasPostInitialized = true;
    }

    private void postInitClient() {
        Class<?> clazz = client.getClass();
        try {
            Method method = clazz.getDeclaredMethod("onPostInit");
            method.invoke(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
