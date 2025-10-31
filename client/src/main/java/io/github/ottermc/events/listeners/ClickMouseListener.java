package io.github.ottermc.events.listeners;

import io.github.ottermc.events.CancelableEvent;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface ClickMouseListener extends Listener {

    void onClickMouse(ClickMouseEvent event);

    class ClickMouseEvent extends CancelableEvent {

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof ClickMouseListener)
                    ((ClickMouseListener) l).onClickMouse(this);
            }
        }
    }
}
