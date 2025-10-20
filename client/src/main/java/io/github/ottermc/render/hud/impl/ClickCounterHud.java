package io.github.ottermc.render.hud.impl;

import io.github.ottermc.render.hud.MovableComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class ClickCounterHud extends MovableComponent {

    public static ClickCounterHud INSTANCE;

    public ClickCounterHud(int width, int height) {
        super(10, 10, width, height);
        INSTANCE = this;
    }

    public static class ClickCounter {

        public final List<Long> clicks = new ArrayList<>();

        protected int getClicks() {
            final long time = System.currentTimeMillis();
            clicks.removeIf(l -> l + 1000 < time);
            return clicks.size();
        }

        public String toString() {
            return getClicks() < 10 ? "0" + getClicks() : getClicks() + "";
        }
    }

    @Override
    public int getSerialId() {
        return "CLICK_COUNTER_COMPONENT".hashCode();
    }
}
