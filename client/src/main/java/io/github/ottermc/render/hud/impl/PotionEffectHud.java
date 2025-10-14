package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

public abstract class PotionEffectHud extends MovableComponent {

    public static PotionEffectHud INSTANCE;

    public PotionEffectHud() {
        super(10, 10, 124, 58);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "POTION_EFFECT_COMPONENT".hashCode();
    }
}
