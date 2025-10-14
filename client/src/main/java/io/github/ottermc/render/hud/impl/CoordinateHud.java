package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

public abstract class CoordinateHud extends MovableComponent {

    public static CoordinateHud INSTANCE;

    public CoordinateHud() {
        super(10, 10, 98, 7);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "COORDINATE_COMPONENT".hashCode();
    }
}
