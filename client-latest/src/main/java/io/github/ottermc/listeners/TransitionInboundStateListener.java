package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.state.NetworkState;

import java.util.List;

public interface TransitionInboundStateListener extends Listener {

    void onTransitionInboundState(TransitionInboundStateEvent event);

    class TransitionInboundStateEvent extends Event {

        private final NetworkState<?> state;
        private final PacketListener listener;

        public TransitionInboundStateEvent(NetworkState<?> state, PacketListener listener) {
            this.state = state;
            this.listener = listener;
        }

        public NetworkState<?> getState() {
            return state;
        }

        public PacketListener getListener() {
            return listener;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof TransitionInboundStateListener)
                    ((TransitionInboundStateListener) l).onTransitionInboundState(this);
            }
        }
    }
}
