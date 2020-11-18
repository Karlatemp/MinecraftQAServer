package io.github.karlatemp.mqaserver.netty;

import com.google.common.base.Preconditions;
import io.github.karlatemp.mqaserver.protocol.MinecraftDecoder;
import io.github.karlatemp.mqaserver.protocol.MinecraftEncoder;
import io.github.karlatemp.mqaserver.protocol.PacketWrapper;
import io.github.karlatemp.mqaserver.protocol.Protocol;
import io.github.karlatemp.mqaserver.protocol.packet.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class ChannelWrapper {

    private final Channel ch;
    private SocketAddress remoteAddress;
    private volatile boolean closed;
    private volatile boolean closing;

    public ChannelWrapper(ChannelHandlerContext ctx) {
        this.ch = ctx.channel();
        this.remoteAddress = (this.ch.remoteAddress() == null) ? this.ch.parent().localAddress() : this.ch.remoteAddress();
    }

    public void setProtocol(Protocol protocol) {
        ch.pipeline().get(MinecraftDecoder.class).setProtocol(protocol);
        ch.pipeline().get(MinecraftEncoder.class).setProtocol(protocol);
    }

    public void setVersion(int protocol) {
        ch.pipeline().get(MinecraftDecoder.class).setProtocolVersion(protocol);
        ch.pipeline().get(MinecraftEncoder.class).setProtocolVersion(protocol);
    }

    public void write(Object packet) {
        if (!closed) {
            if (packet instanceof PacketWrapper) {
                ((PacketWrapper) packet).setReleased(true);
                ch.writeAndFlush(((PacketWrapper) packet).buf, ch.voidPromise());
            } else {
                ch.writeAndFlush(packet, ch.voidPromise());
            }
        }
    }

    public void markClosed() {
        closed = closing = true;
    }

    public void close() {
        close(null);
    }

    public void close(Object packet) {
        if (!closed) {
            closed = closing = true;

            if (packet != null && ch.isActive()) {
                ch.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE, ChannelFutureListener.CLOSE);
            } else {
                ch.flush();
                ch.close();
            }
        }
    }

    public void delayedClose(final Kick kick) {
        if (!closing) {
            closing = true;

            // Minecraft client can take some time to switch protocols.
            // Sending the wrong disconnect packet whilst a protocol switch is in progress will crash it.
            // Delay 250ms to ensure that the protocol switch (if any) has definitely taken place.
            ch.eventLoop().schedule(new Runnable() {

                @Override
                public void run() {
                    close(kick);
                }
            }, 250, TimeUnit.MILLISECONDS);
        }
    }

    public void addBefore(String baseName, String name, ChannelHandler handler) {
        Preconditions.checkState(ch.eventLoop().inEventLoop(), "cannot add handler outside of event loop");
        ch.pipeline().flush();
        ch.pipeline().addBefore(baseName, name, handler);
    }

    public Channel getHandle() {
        return ch;
    }

    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isClosing() {
        return this.closing;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
