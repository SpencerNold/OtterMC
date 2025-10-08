package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.packet.Packet;

import java.util.List;

public interface EncodePacketListener extends Listener {

    void onEncodePacket(EncodePacketEvent event);

    class EncodePacketEvent extends Event {

        private final ChannelHandlerContext context;
        private final Packet<?> packet;
        private final ByteBuf buf;
        private boolean canceled = false;

        public EncodePacketEvent(ChannelHandlerContext context, Packet<?> packet, ByteBuf buf) {
            this.context = context;
            this.packet = packet;
            this.buf = buf;
        }

        public ChannelHandlerContext getContext() {
            return context;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public ByteBuf getBuffer() {
            return buf;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        @Override
        public void fire(List<Listener> listeners) {
            for (Listener l : listeners) {
                if (l instanceof EncodePacketListener)
                    ((EncodePacketListener) l).onEncodePacket(this);
            }
        }
    }
}
