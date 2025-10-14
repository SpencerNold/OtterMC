package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

public abstract class ArmorStatusHud extends MovableComponent {

    public static ArmorStatusHud INSTANCE;

    public ArmorStatusHud() {
        super(10, 10, 64, 16);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "ARMOR_STATUS_COMPONENT".hashCode();
    }
}
