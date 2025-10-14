package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

public abstract class KeyStrokeHud extends MovableComponent {

    public static KeyStrokeHud INSTANCE;

    public KeyStrokeHud() {
        super(10, 10, 58, 51);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "KEYSTROKE_COMPONENT".hashCode();
    }
}
