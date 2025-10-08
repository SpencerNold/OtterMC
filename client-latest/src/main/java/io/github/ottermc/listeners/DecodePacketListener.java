package io.github.ottermc.listeners;

import io.github.ottermc.events.Event;
import io.github.ottermc.events.Listener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface DecodePacketListener extends Listener {

    void onDecodePacket(DecodePacketEvent event);

    class DecodePacketEvent extends Event {

        private final ChannelHandlerContext context;
        private final ByteBuf buf;
        private final List<Object> objects;
        private boolean canceled = false;

        public DecodePacketEvent(ChannelHandlerContext context, ByteBuf buf, List<Object> objects) {
            this.context = context;
            this.buf = buf;
            this.objects = objects;
        }

        public ChannelHandlerContext getContext() {
            return context;
        }

        public ByteBuf getBuffer() {
            return buf;
        }

        public List<Object> getObjects() {
            return objects;
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
                if (l instanceof DecodePacketListener)
                    ((DecodePacketListener) l).onDecodePacket(this);
            }
        }
    }
}
