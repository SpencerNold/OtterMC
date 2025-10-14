package io.github.ottermc.modules.impl.utility;

import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.universal.UGameSettings;

public class Fullbright extends Module {

    public Fullbright() {
        super("Fullbright", CategoryList.GAME);
    }

    @Override
    public void onEnable() {
        UGameSettings.setGamma(16.0f);
    }

    @Override
    public String getDescription() {
        return "Increases the game brightness to see as if there were no shadows or darkness.";
    }
}
