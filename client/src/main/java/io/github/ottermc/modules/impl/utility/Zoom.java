package io.github.ottermc.modules.impl.utility;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.events.listeners.RunTickListener;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.KeyboardSetting;
import io.github.ottermc.universal.*;

public class Zoom extends Module implements RunTickListener {

    private final KeyboardSetting setting = new KeyboardSetting("Zoom Key", UKeyRegistry.getKeyC());

    private boolean zooming;
    private float lastFov;

    public Zoom() {
        super("Zoom", CategoryList.GAME);
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

    @Override
    public void onRunTick(RunTickEvent event) {
        Object currentScreen = UMinecraft.getCurrentScreen();
        if (currentScreen != null)
            return;
        boolean down = UKeyboard.isKeyDown(setting.getValue());
        if (down && !zooming) {
            float fov = getTargetFovFromVersion();
            zooming = true;
            UGameSettings.setSmoothCamera(true);
            lastFov = UGameSettings.getFieldOfView();
            UGameSettings.setFieldOfView(fov);
        }
        if (!down && zooming) {
            zooming = false;
            UGameSettings.setSmoothCamera(false);
            UGameSettings.setFieldOfView(lastFov);
        }
    }

    private float getTargetFovFromVersion() {
        switch (UVersion.getClientVersion()) {
            case UVersion.V8:
                return 19.0f;
            case UVersion.V21:
                return 32;
        }
        throw new IllegalStateException("missing proper version for Zoom mod");
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{setting};
    }
}
