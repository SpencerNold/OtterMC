package io.github.ottermc.universal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class ClientGameSettings extends UGameSettings {

    @Override
    protected void _setGamma(float gamma) {
        getGameSettings().gammaSetting = gamma;
    }

    @Override
    protected void _setSmoothCamera(boolean smooth) {
        getGameSettings().smoothCamera = smooth;
    }

    @Override
    protected void _setFieldOfView(float fov) {
        getGameSettings().fovSetting = fov;
    }

    @Override
    protected float _getFieldOfView() {
        return getGameSettings().fovSetting;
    }

    private GameSettings getGameSettings() {
        return Minecraft.getMinecraft().gameSettings;
    }
}
