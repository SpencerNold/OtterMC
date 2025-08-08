package io.github.ottermc.smp.modules.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.client.render.RenderTickCounter;

import java.util.List;

public interface RenderWorldListener extends Listener {

    void onRenderWorld(RenderWorldEvent event);

    class RenderWorldEvent extends Event {

        private final RenderTickCounter counter;

        public RenderWorldEvent(RenderTickCounter counter) {
            this.counter = counter;
        }

        public RenderTickCounter getCounter() {
            return counter;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof RenderWorldListener)
                    ((RenderWorldListener) l).onRenderWorld(this);
            }
        }
    }
}
