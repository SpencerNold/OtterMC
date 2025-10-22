package io.github.fabric.universal;

import io.github.ottermc.universal.UGameSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class ClientGameSettings extends UGameSettings {
    @Override
    protected void _setGamma(float gamma) {
        getGameSettings().getGamma().setValue((double) gamma);
    }

    @Override
    protected void _setSmoothCamera(boolean smooth) {
        getGameSettings().smoothCameraEnabled = smooth;
    }

    @Override
    protected void _setFieldOfView(float fov) {
        getGameSettings().getFov().setValue((int) fov);
    }

    @Override
    protected float _getFieldOfView() {
        return getGameSettings().getFov().getValue();
    }

    private GameOptions getGameSettings() {
        return MinecraftClient.getInstance().options;
    }
}
