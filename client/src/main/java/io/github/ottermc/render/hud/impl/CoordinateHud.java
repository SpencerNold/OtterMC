package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;
import io.github.ottermc.universal.UDrawable;

public abstract class CoordinateHud extends MovableComponent {

    public static CoordinateHud INSTANCE;

    public CoordinateHud() {
        super(10, 10, 100, 9);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "COORDINATE_COMPONENT".hashCode();
    }
}
