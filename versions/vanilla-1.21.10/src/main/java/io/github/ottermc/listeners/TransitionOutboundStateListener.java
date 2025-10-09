package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.network.state.NetworkState;

import java.util.List;

public interface TransitionOutboundStateListener extends Listener {

    void onTransitionOutboundState(TransitionOutboundStateEvent event);

    class TransitionOutboundStateEvent extends Event {

        private final NetworkState<?> state;

        public TransitionOutboundStateEvent(NetworkState<?> state) {
            this.state = state;
        }

        public NetworkState<?> getState() {
            return state;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof TransitionOutboundStateListener)
                    ((TransitionOutboundStateListener) l).onTransitionOutboundState(this);
            }
        }
    }
}
