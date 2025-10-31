package io.github.ottermc.events.listeners;

import io.github.ottermc.events.CancelableEvent;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface RightClickMouseListener extends Listener {

    void onRightClickMouse(RightClickMouseEvent event);

    class RightClickMouseEvent extends CancelableEvent {
        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof RightClickMouseListener)
                    ((RightClickMouseListener) l).onRightClickMouse(this);
            }
        }
    }
}
