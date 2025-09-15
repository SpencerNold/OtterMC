package io.github.ottermc.pvp.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface ClickMouseListener extends Listener {

    void onClickMouse(ClickMouseEvent event);

    class ClickMouseEvent extends Event {
        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof ClickMouseListener)
                    ((ClickMouseListener) l).onClickMouse(this);
            }
        }
    }
}
