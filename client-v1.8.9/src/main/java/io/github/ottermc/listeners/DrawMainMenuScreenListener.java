package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface DrawMainMenuScreenListener extends Listener {

    void onDrawMainMenuScreen(DrawMainMenuScreenEvent event);

    class DrawMainMenuScreenEvent extends Event {

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof DrawMainMenuScreenListener)
                    ((DrawMainMenuScreenListener) l).onDrawMainMenuScreen(this);
            }
        }
    }
}
