package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;
import io.github.ottermc.universal.UDrawable;

public class ToggleSprintHud extends MovableComponent {

    public static ToggleSprintHud INSTANCE;

    private boolean enabled = false;

    public ToggleSprintHud() {
        super(10, 10, 100, 9);
        INSTANCE = this;
    }


    @Override
    public void draw(Object context) {
        UDrawable.drawString(context, String.format("[sprint: %s]", enabled ? "on" : "off"), getDefaultX(), getDefaultY(), -1, false);
    }

    @Override
    public void drawDummyObject(Object context) {
        UDrawable.drawString(context, "[sprint: on]", getDefaultX(), getDefaultY(), -1, false);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getSerialId() {
        return "TOGGLE_SPRINT_COMPONENT".hashCode();
    }
}
