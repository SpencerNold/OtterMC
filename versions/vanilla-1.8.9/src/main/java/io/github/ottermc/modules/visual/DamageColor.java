package io.github.ottermc.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.listeners.SetEntityDamageBrightnessListener;
import io.github.ottermc.modules.CategoryList;
import io.github.ottermc.render.Color;

public class DamageColor extends Module implements SetEntityDamageBrightnessListener {

    private final ColorSetting color = new ColorSetting("Color", Color.getDefault(), false);

    public DamageColor() {
        super("Damage Color", CategoryList.VISUAL);
    }

    @Override
    public void onEnable() {
        EventBus.add(this);
    }

    @Override
    public void onDisable() {
        EventBus.remove(this);
    }

    @Override
    public void onSetEntityDamageBrightness(SetEntityDamageBrightnessEvent event) {
        Color color = this.color.getValue();
        event.setRed(color.getRedNormal());
        event.setGreen(color.getGreenNormal());
        event.setBlue(color.getBlueNormal());
    }

    @Override
    public Storable<?>[] getWritables() {
        return new Storable<?>[]{color};
    }
}
