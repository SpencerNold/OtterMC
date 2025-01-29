package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public interface DrawOverlayListener extends Listener {

    void onDrawOverlay(DrawOverlayEvent event);

    class DrawOverlayEvent extends Event {

        private final DrawContext context;

        public DrawOverlayEvent(DrawContext context) {
            this.context = context;
        }

        public DrawContext getContext() {
            return context;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof DrawOverlayListener)
                    ((DrawOverlayListener) l).onDrawOverlay(this);
            }
        }
    }
}
