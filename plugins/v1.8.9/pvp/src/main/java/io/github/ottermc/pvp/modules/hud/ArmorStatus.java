package io.github.ottermc.pvp.modules.hud;

import io.github.ottermc.modules.Module;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;
import io.github.ottermc.screen.render.Icon;

public class ArmorStatus extends Module {

    private static final Icon ICON = Icon.getIconIgnoreException("module/armor_icon.png");

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

    @Override
    public Icon getIcon() {
        return ICON;
    }
}
