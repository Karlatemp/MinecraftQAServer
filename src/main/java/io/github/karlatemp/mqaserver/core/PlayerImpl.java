package io.github.karlatemp.mqaserver.core;

import io.github.karlatemp.mqaserver.handlers.InitialHandler;
import io.github.karlatemp.mqaserver.netty.ChannelWrapper;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.PacketWrapper;
import io.github.karlatemp.mqaserver.protocol.packet.Chat;
import io.github.karlatemp.mqaserver.protocol.packet.JoinGame;
import io.github.karlatemp.mqaserver.protocol.packet.Kick;
import io.github.karlatemp.mqaserver.protocol.packet.Teleport;
import io.github.karlatemp.mqaserver.utils.Log;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerImpl implements IPlayer {
    private final ChannelWrapper ch;
    private final String name;
    private final InitialHandler handler;
    public Object extData;
    // private Teleport latestTeleport;

    public PlayerImpl(ChannelWrapper ch, String name, InitialHandler handler) {
        this.ch = ch;
        this.name = name;
        this.handler = handler;
    }

    @Override
    public void disconnect(String reason) {
        disconnect(TextComponent.fromLegacyText(reason));
    }

    @Override
    public void disconnect(BaseComponent... reason) {
        if (!ch.isClosing()) {
            Log.getLogger().log(Level.INFO, "[{0}] disconnected with: {1}", new Object[]{
                    getName(), BaseComponent.toLegacyText(reason)
            });

            ch.close(new Kick(ComponentSerializer.toString(reason)));
        }
    }

    @Override
    public void sendPacket(DefinedPacket packet) {
        /*if (packet instanceof Teleport) {
            latestTeleport = (Teleport) packet;
        }*/
        ch.write(packet);
    }

    @Override
    public void sendPacket(PacketWrapper packet) {
        sendPacket(packet.packet);
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return handler.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        sendMessage(TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        sendMsg((byte) 1, message);
    }

    private void sendMsg(byte type, BaseComponent[] msg) {
        sendPacket(new Chat(ComponentSerializer.toString(msg), type));
    }

    @Override
    public void sendActionBar(BaseComponent... message) {
        sendMsg((byte) 2, message);
    }

    public ChannelWrapper getCh() {
        return ch;
    }

    public InitialHandler getHandler() {
        return handler;
    }

    @Override
    public void teleport(double x, double y, double z) {
        sendPacket(new Teleport(x, y, z));
    }

    /*@Override
    public void changeWorld(JoinGame.WorldType type) {
        sendPacket(new JoinGame(type));
        if (latestTeleport != null) {
            sendPacket(latestTeleport);
            sendPacket(latestTeleport);
        } else {
            sendPacket(new Teleport());
            sendPacket(new Teleport());
        }
    }*/
}
