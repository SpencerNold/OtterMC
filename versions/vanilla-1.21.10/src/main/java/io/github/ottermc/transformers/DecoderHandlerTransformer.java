package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.DecodePacketListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.spencernold.transformer.*;
import net.minecraft.network.handler.DecoderHandler;
import net.minecraft.network.handler.NetworkStateTransitionHandler;
import net.minecraft.network.state.NetworkState;

import java.util.List;

@Transformer(className = "net/minecraft/network/handler/DecoderHandler")
public class DecoderHandlerTransformer {

    @Injector(target = Target.HEAD, name = "decode(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List;)V")
    public void onDecode(DecoderHandler<?> handler, ChannelHandlerContext context, ByteBuf buf, List<Object> objects, Callback callback) {
        DecodePacketListener.DecodePacketEvent event = new DecodePacketListener.DecodePacketEvent(context, buf, objects);
        EventBus.fire(event);
        callback.setCanceled(event.isCanceled());
    }
}
