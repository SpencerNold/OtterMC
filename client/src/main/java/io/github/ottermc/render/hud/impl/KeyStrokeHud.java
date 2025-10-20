package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

public abstract class KeyStrokeHud extends MovableComponent {

    public static KeyStrokeHud INSTANCE;

    public KeyStrokeHud(int width, int height) {
        super(10, 10, width, height);
        INSTANCE = this;
    }

    @Override
    public int getSerialId() {
        return "KEYSTROKE_COMPONENT".hashCode();
    }
}
