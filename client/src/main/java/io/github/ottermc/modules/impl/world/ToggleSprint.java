package io.github.ottermc.modules.impl.world;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.render.hud.impl.ToggleSprintHud;
import io.github.ottermc.universal.UGameSettings;
import io.github.ottermc.universal.UPlayer;

public class ToggleSprint extends Module implements RunTickListener {

    private boolean pressed = false, enabled = false;

    public ToggleSprint() {
        super("Toggle Sprint", CategoryList.WORLD);
    }

    @Override
    public void onEnable() {
        EventBus.add(this);
        ToggleSprintHud.INSTANCE.setVisible(true);
    }

    @Override
    public void onDisable() {
        EventBus.remove(this);
        ToggleSprintHud.INSTANCE.setVisible(false);
    }

    @Override
    public void onRunTick(RunTickEvent event) {
        if (!pressed && UGameSettings.isSprintKeyDown()) {
            pressed = true;
            enabled = !enabled;
            ToggleSprintHud.INSTANCE.setEnabled(enabled);
        }
        if (pressed && !UGameSettings.isSprintKeyDown())
            pressed = false;
        if (UPlayer.isSprintViable() && enabled)
            UPlayer.setSprinting(true);
    }
}
