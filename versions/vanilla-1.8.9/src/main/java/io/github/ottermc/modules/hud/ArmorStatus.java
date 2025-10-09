package io.github.ottermc.modules.hud;

import io.github.ottermc.hud.ClientDisplay;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;

public class ArmorStatus extends Module {

    public ArmorStatus() {
        super("Armor Status", CategoryList.DISPLAY);
    }

    @Override
    public void onEnable() {
        ClientDisplay.ARMOR_STATUS.setVisible(true);
    }

    @Override
    public void onDisable() {
        ClientDisplay.ARMOR_STATUS.setVisible(false);
    }

}
