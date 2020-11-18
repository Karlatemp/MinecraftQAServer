package io.github.karlatemp.mqaserver.core;

import com.google.gson.Gson;
import io.github.karlatemp.mqaserver.handlers.UpstreamBridge;
import io.github.karlatemp.mqaserver.netty.PipelineUtils;
import io.github.karlatemp.mqaserver.protocol.packet.JoinGame;
import io.github.karlatemp.mqaserver.utils.Log;
import io.github.karlatemp.mqaserver.utils.ResultCallback;
import io.github.karlatemp.mqaserver.utils.ServerPing;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface ISystemCore {
    static ISystemCore getInstance() {
        return Companion.getInstance();
    }

    static Logger getLogger() {
        return Log.getLogger();
    }

    static IPlayerManager getPlayerManager() {
        return getInstance().getPlayerManager0();
    }

    class Companion {
        private static ISystemCore instance;

        public static void setInstance(ISystemCore instance) {
            Companion.instance = instance;
        }

        public static ISystemCore getInstance() {
            return instance;
        }

        public static void startServer() {
            ChannelFutureListener listener = future -> {
                if (future.isSuccess()) {
                    getLogger().log(Level.INFO, "Listening on {0}", instance.getSocketAddress());
                } else {
                    getLogger().log(Level.WARNING, "Could not bind to host " + instance.getSocketAddress(), future.cause());
                }
            };
            new ServerBootstrap()
                    .channel(PipelineUtils.getServerChannel(instance.getSocketAddress()))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(PipelineUtils.SERVER_CHILD)
                    .group(instance.getEventLoopGroup())
                    .localAddress(instance.getSocketAddress())
                    .bind().addListener(listener);
        }
    }

    boolean isSupportedProtocol(int protocolVersion);

    IPlayerManager getPlayerManager0();


    int getProtocolVersion();

    EventLoopGroup getEventLoopGroup();

    boolean isOnlineMode();

    ServerPing newServerPing(int protocolVersion);

    Gson getGson();

    boolean isPreventProxyConnections();

    SocketAddress getSocketAddress();

    void httpGet(String url, ResultCallback<String> callback);

    UpstreamBridge newUpstreamBridge(PlayerImpl player);

    double[] newJoinLocation();

    JoinGame.WorldType worldType();
}
