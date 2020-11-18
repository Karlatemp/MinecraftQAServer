package io.github.karlatemp.mqaserver.utils;

import com.google.common.base.Preconditions;
import io.github.karlatemp.mqaserver.handlers.PingHandler;
import io.github.karlatemp.mqaserver.netty.ChannelWrapper;
import io.github.karlatemp.mqaserver.netty.PacketHandler;
import io.github.karlatemp.mqaserver.protocol.PacketWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Level;

public class HandlerBoss extends ChannelInboundHandlerAdapter {

    private ChannelWrapper channel;
    private PacketHandler handler;

    public void setHandler(PacketHandler handler) {
        Preconditions.checkArgument(handler != null, "handler");
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            channel = new ChannelWrapper(ctx);
            handler.connected(channel);

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            channel.markClosed();
            handler.disconnected(channel);

        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            handler.writabilityChanged(channel);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (handler != null) {
            PacketWrapper packet = (PacketWrapper) msg;
            boolean sendPacket = handler.shouldHandle(packet);
            try {
                if (sendPacket && packet.packet != null) {
                    try {
                        packet.packet.handle(handler);
                    } catch (CancelSendSignal ex) {
                        sendPacket = false;
                    }
                }
                if (sendPacket) {
                    handler.handle(packet);
                }
            } finally {
                packet.trySingleRelease();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            boolean logExceptions = !(handler instanceof PingHandler);

            if (logExceptions) {
            }

            if (handler != null) {
                try {
                    handler.exception(cause);
                } catch (Exception ex) {
                    Log.getLogger().log(Level.SEVERE, handler + " - exception processing exception", ex);
                }
            }

            ctx.close();
        }
    }
}
