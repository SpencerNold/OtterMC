package io.github.ottermc.modules.impl.hud;

import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.render.hud.impl.ArmorStatusHud;

public class ArmorStatus extends Module {

    public ArmorStatus() {
        super("Armor Status", CategoryList.DISPLAY);
    }

    @Override
    public void onEnable() {
        ArmorStatusHud.INSTANCE.setVisible(true);
    }

    @Override
    public void onDisable() {
        ArmorStatusHud.INSTANCE.setVisible(false);
    }

}
