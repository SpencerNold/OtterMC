package io.github.ottermc.modules.impl.display;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RenderGameOverlayListener;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;

public class Waypoints extends Module implements RenderGameOverlayListener {

    public Waypoints() {
        super("Waypoints", CategoryList.DISPLAY);
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
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    }
}
