package io.github.ottermc.events.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;

import java.util.List;

public interface DrawMainMenuScreenListener extends Listener {

    public void onDrawMainMenuScreen(DrawMainMenuScreenEvent event);

    public static class DrawMainMenuScreenEvent extends Event {

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof DrawMainMenuScreenListener)
                    ((DrawMainMenuScreenListener) l).onDrawMainMenuScreen(this);
            }
        }
    }
}
