package io.github.karlatemp.mqaserver.core;

import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.PacketWrapper;
import io.github.karlatemp.mqaserver.protocol.packet.JoinGame;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IPlayer {
    void disconnect(String reason);

    void disconnect(BaseComponent... reason);

    void sendPacket(DefinedPacket packet);

    void sendPacket(PacketWrapper packet);

    @NotNull
    String getName();

    @NotNull
    UUID getUniqueId();

    void sendMessage(String message);

    void sendMessage(BaseComponent... message);

    void sendActionBar(BaseComponent... message);

    void teleport(double x, double y, double z);

    default void teleport(int x, int y, int z) {
        teleport((double) x, (double) y, (double) z);
    }
//    void changeWorld(JoinGame.WorldType type);
}
