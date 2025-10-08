package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

import java.util.List;

public interface PacketSendListener extends Listener {

    void onPacketSend(PacketSendEvent event);

    class PacketSendEvent extends Event {

        private final ClientConnection connection;
        private final Packet<?> packet;
        private final Object channelFutureListener;
        private final boolean flush;

        public PacketSendEvent(ClientConnection connection, Packet<?> packet, Object channelFutureListener, boolean flush) {
            this.connection = connection;
            this.packet = packet;
            this.channelFutureListener = channelFutureListener;
            this.flush = flush;
        }

        public ClientConnection getConnection() {
            return connection;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public Object getChannelFutureListener() {
            return channelFutureListener;
        }

        public boolean shouldFlush() {
            return flush;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof PacketSendListener)
                    ((PacketSendListener) l).onPacketSend(this);
            }
        }
    }
}
