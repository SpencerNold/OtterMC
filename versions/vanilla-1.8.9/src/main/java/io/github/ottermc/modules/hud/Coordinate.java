package io.github.ottermc.modules.hud;

import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.hud.ClientDisplay;
import io.github.ottermc.render.Color;

public class Coordinate extends Module {

    private static Coordinate instance;

    private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);
    private final BooleanSetting ttf = new BooleanSetting("TrueType Font", false);

    public Coordinate() {
        super("Coordinates", CategoryList.DISPLAY);
        instance = this;
    }

    @Override
    public void onEnable() {
        ClientDisplay.COORDINATE.setVisible(true);
    }

    @Override
    public void onDisable() {
        ClientDisplay.COORDINATE.setVisible(false);
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{color, ttf};
    }

    public static Color getColor() {
        return instance.color.getValue();
    }

    public static boolean shouldUseClientFont() {
        return instance.ttf.getValue();
    }
}
