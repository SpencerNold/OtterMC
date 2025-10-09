package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

import java.util.List;

public interface PacketReceiveListener extends Listener {

    void onPacketReceive(PacketReceiveEvent event);

    class PacketReceiveEvent extends Event {

        private final Packet<?> packet;
        private final PacketListener listener;

        public PacketReceiveEvent(Packet<?> packet, PacketListener listener) {
            this.packet = packet;
            this.listener = listener;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public PacketListener getListener() {
            return listener;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof PacketReceiveListener)
                    ((PacketReceiveListener) l).onPacketReceive(this);
            }
        }
    }
}
