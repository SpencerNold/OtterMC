package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.EncodePacketListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.network.handler.EncoderHandler;
import net.minecraft.network.packet.Packet;

@Transformer(className = "net/minecraft/network/handler/EncoderHandler")
public class EncoderHandlerTransformer {

    @Injector(target = Target.HEAD, name = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;Lio/netty/buffer/ByteBuf;)V")
    public void onEncode(EncoderHandler<?> encoder, ChannelHandlerContext context, Packet<?> packet, ByteBuf buf, Callback callback) {
        EncodePacketListener.EncodePacketEvent event = new EncodePacketListener.EncodePacketEvent(context, packet, buf);
        EventBus.fire(event);
        callback.setCanceled(event.isCanceled());
    }
}

