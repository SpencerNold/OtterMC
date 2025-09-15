package io.github.ottermc.pvp.modules.hud;

import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.pvp.screen.hud.ClientDisplay;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class KeyStroke extends Module {

    private static final Icon ICON = Icon.getIconIgnoreException("module/keyboard_icon.png");

    private static KeyStroke instance;

    private final ColorSetting color = new ColorSetting("Color", new Color(-1), false);

    public KeyStroke() {
        super("Keystrokes", CategoryList.DISPLAY);
        instance = this;
    }

    @Override
    public void onEnable() {
        ClientDisplay.KEYSTROKE.setVisible(true);
    }

    @Override
    public void onDisable() {
        ClientDisplay.KEYSTROKE.setVisible(false);
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{color};
    }

    public static Color getColor() {
        return instance.color.getValue();
    }
}
