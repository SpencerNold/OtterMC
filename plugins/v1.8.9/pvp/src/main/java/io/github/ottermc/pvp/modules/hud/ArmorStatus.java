package io.github.ottermc.pvp.modules.hud;

import io.github.ottermc.modules.Module;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;

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
