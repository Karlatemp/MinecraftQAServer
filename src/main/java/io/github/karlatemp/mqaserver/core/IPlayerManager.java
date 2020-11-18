package io.github.karlatemp.mqaserver.core;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IPlayerManager {
    static IPlayerManager instance() {
        return ISystemCore.getPlayerManager();
    }

    @Nullable
    IPlayer getPlayer(String name);

    @Nullable
    IPlayer getPlayer(UUID uuid);

    void putConnection(IPlayer player);

    void removeConnection(IPlayer player);
}
