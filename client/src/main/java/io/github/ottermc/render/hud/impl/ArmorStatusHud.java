package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

public abstract class ArmorStatusHud extends MovableComponent {

    public static ArmorStatusHud INSTANCE;

    public ArmorStatusHud(int width, int height) {
        super(10, 10, width, height);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "ARMOR_STATUS_COMPONENT".hashCode();
    }
}
