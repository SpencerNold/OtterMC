package io.github.ottermc.transformers;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.listeners.PacketReceiveListener;
import io.github.ottermc.listeners.PacketSendListener;
import io.github.ottermc.listeners.TransitionInboundStateListener;
import io.github.ottermc.listeners.TransitionOutboundStateListener;
import me.spencernold.transformer.Callback;
import me.spencernold.transformer.Injector;
import me.spencernold.transformer.Target;
import me.spencernold.transformer.Transformer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.state.NetworkState;

@Transformer(className = "net/minecraft/network/ClientConnection")
public class ClientConnectionTransformer {

    @Injector(target = Target.HEAD, name = "send(Lnet/minecraft/network/packet/Packet;Lio/netty/channel/ChannelFutureListener;Z)V")
    public void onSend(ClientConnection connection, Packet<?> packet, Object channelFutureListener, boolean flush, Callback callback) {
        EventBus.fire(new PacketSendListener.PacketSendEvent(connection, packet, channelFutureListener, flush));
    }

    @Injector(target = Target.HEAD, name = "handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V")
    public void onHandlePacket(ClientConnection connection, Packet<?> packet, PacketListener listener, Callback callback) {
        EventBus.fire(new PacketReceiveListener.PacketReceiveEvent(packet, listener));
    }

    @Injector(target = Target.HEAD, name = "transitionInbound(Lnet/minecraft/network/state/NetworkState;Lnet/minecraft/network/listener/PacketListener;)V")
    public void onTransitionInbound(ClientConnection connection, NetworkState<?> state, PacketListener listener, Callback callback) {
        EventBus.fire(new TransitionInboundStateListener.TransitionInboundStateEvent(state, listener));
    }

    @Injector(target = Target.HEAD, name = "transitionOutbound(Lnet/minecraft/network/state/NetworkState;)V")
    public void onTransitionOutbound(ClientConnection connection, NetworkState<?> state, Callback callback) {
        EventBus.fire(new TransitionOutboundStateListener.TransitionOutboundStateEvent(state));
    }
}
