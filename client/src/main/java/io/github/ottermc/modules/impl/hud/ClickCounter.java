package io.github.ottermc.modules.impl.hud;

import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.render.Color;
import io.github.ottermc.render.hud.impl.ClickCounterHud;

public class ClickCounter extends Module {

    private static ClickCounter instance;

    private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);

    public ClickCounter() {
        super("CPS", CategoryList.DISPLAY);
        instance = this;
    }

    @Override
    public void onEnable() {
        ClickCounterHud.INSTANCE.setVisible(true);
    }

    @Override
    public void onDisable() {
        ClickCounterHud.INSTANCE.setVisible(false);
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{color};
    }

    public static Color getColor() {
        return instance.color.getValue();
    }
}
