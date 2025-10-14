package io.github.ottermc.modules.impl.hud;

import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.render.Color;
import io.github.ottermc.render.hud.impl.KeyStrokeHud;

public class KeyStroke extends Module {

    private static KeyStroke instance;

    private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);

    public KeyStroke() {
        super("Keystrokes", CategoryList.DISPLAY);
        instance = this;
    }

    @Override
    public void onEnable() {
        KeyStrokeHud.INSTANCE.setVisible(true);
    }

    @Override
    public void onDisable() {
        KeyStrokeHud.INSTANCE.setVisible(false);
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{color};
    }

    public static Color getColor() {
        return instance.color.getValue();
    }
}
