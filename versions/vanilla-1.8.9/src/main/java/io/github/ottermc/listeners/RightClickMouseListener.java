package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RightClickMouseListener extends Listener {

    void onRightClickMouse(RightClickMouseEvent event);

    class RightClickMouseEvent extends Event {
        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof RightClickMouseListener)
                    ((RightClickMouseListener) l).onRightClickMouse(this);
            }
        }
    }
}
